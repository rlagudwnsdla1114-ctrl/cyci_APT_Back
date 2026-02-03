package kr.soft.apt.service.AI;

import kr.soft.apt.dto.AI.AIComMatch.*;
import kr.soft.apt.dto.AI.AIInterview.CoverPostsDTO;
import kr.soft.apt.dto.AI.AIInterview.InterviewResultDTO;
import kr.soft.apt.dto.AI.AIMatch.*;
import kr.soft.apt.mapper.AI.AIMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class AIService {

    // r구직자 AI 매칭
    private final AIMapper aiMapper;
    private final RestTemplate restTemplate;

    private final String aiServer = "http://localhost:8081";

    public AIService(AIMapper aiMapper, RestTemplate restTemplate) {
        this.aiMapper = aiMapper;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public List<AIRecommendedCompanyDTO> aiSearch(long jobseekerIdx, int topN) {

        CoverPosts cover = aiMapper.coverposts(jobseekerIdx);
        if (cover == null) return List.of();

        List<JobPosts> jobposts = aiMapper.jobposts();
        if (jobposts == null || jobposts.isEmpty()) return List.of();

        int safeTopN = Math.max(1, Math.min(topN, 20));

        Long coverPostsIdx = aiMapper.selectLatestCoverPostsIdx(jobseekerIdx);
        if (coverPostsIdx == null || coverPostsIdx <= 0) {
            // coverposts()가 null은 아닌데 PK가 실제로 없거나, 조회 기준이 꼬인 상태
            return List.of();
        }

        AIMatchRequest req = new AIMatchRequest();
        req.setJobseekerIdx(jobseekerIdx);
        req.setTopN(safeTopN);
        req.setCoverPost(cover);
        req.setJobPosts(jobposts);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AIMatchRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<AIMatchResponse> res = restTemplate.exchange(
                aiServer + "/match/jobseeker",
                HttpMethod.POST,
                entity,
                AIMatchResponse.class
        );

        AIMatchResponse body = res.getBody();
        List<AIMatchResponse.Result> results = (body == null ? null : body.getResults());
        if (results == null || results.isEmpty()) return List.of();

        aiMapper.jobmatchdelete(jobseekerIdx);

        for (AIMatchResponse.Result r : results) {
            if (r == null) continue;

            long companyIdx = r.getCompanyIdx();
            long jobPostsIdx = r.getJobPostsIdx();
            if (companyIdx <= 0 || jobPostsIdx <= 0) continue;

            JobseekerMatchingInsertDTO dto = new JobseekerMatchingInsertDTO();

            dto.setScore((int) Math.round(r.getScore()));

            dto.setReason(r.getReason());
            dto.setJobseekerIdx(jobseekerIdx);
            dto.setCompanyIdx(companyIdx);
            dto.setJobPostsIdx(jobPostsIdx);

            dto.setCoverPostsIdx(coverPostsIdx);

            aiMapper.jobmatchinsert(dto);
        }

        return aiMapper.jobmatchselect(jobseekerIdx, safeTopN);
    }

    // AI 구직자 면접
    public Map<String, Object> jobQuestion(long jobseekerIdx) {
        CoverPostsDTO coverLetter = aiMapper.interjobidx(jobseekerIdx);
        if (coverLetter == null) {
            throw new RuntimeException("등록된 자기소개서 없음");
        }

        String coverContent = String.format(
                "지원동기: %s, 성장과정: %s, 성격: %s, 직무경험: %s",
                coverLetter.getApplyMotive(),
                coverLetter.getGrowthProcess(),
                coverLetter.getPersonality(),
                coverLetter.getJobExperience()
        );

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("job", String.valueOf(coverLetter.getHopeJob()));
        requestBody.put("cover_content", coverContent);

        Map<String, Object> pyResp = restTemplate.postForObject(
                aiServer + "/interview/questions",
                requestBody,
                Map.class
        );

        List<String> questions = List.of();
        if (pyResp != null && pyResp.get("questions") instanceof List<?> qList) {
            questions = qList.stream().map(String::valueOf).toList();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("interviewId", coverLetter.getCoverPostsIdx());
        result.put("questions", questions);
        return result;
    }

    @Transactional
    public Map<String, Object> processEvaluation(Long interviewId, Integer questionId,
                                                 Double silenceDuration, Double speakingDuration,
                                                 MultipartFile audioFile) {

        double silence = (silenceDuration == null) ? 0.0 : silenceDuration;
        double speaking = (speakingDuration == null) ? 0.0 : speakingDuration;

        CoverPostsDTO cover = aiMapper.selectCoverByCoverPostsIdx(interviewId);
        if (cover == null) throw new RuntimeException("자기소개서 조회 실패 (coverPostsIdx=" + interviewId + ")");

        String coverContent = String.format(
                "지원동기: %s, 성장과정: %s, 성격: %s, 직무경험: %s",
                cover.getApplyMotive(),
                cover.getGrowthProcess(),
                cover.getPersonality(),
                cover.getJobExperience()
        );

        // 파이썬 서버로 multipart 전송
        org.springframework.util.LinkedMultiValueMap<String, Object> body = new org.springframework.util.LinkedMultiValueMap<>();
        body.add("interviewId", String.valueOf(interviewId));
        body.add("questionId", String.valueOf(questionId));
        body.add("silenceDuration", String.valueOf(silence));
        body.add("speakingDuration", String.valueOf(speaking));
        body.add("cover_content", coverContent);

        try {
            org.springframework.core.io.ByteArrayResource fileAsResource = new org.springframework.core.io.ByteArrayResource(audioFile.getBytes()) {
                @Override
                public String getFilename() {
                    return audioFile.getOriginalFilename() != null ? audioFile.getOriginalFilename() : "answer.webm";
                }
            };
            body.add("audioFile", fileAsResource);

        } catch (Exception e) {
            throw new RuntimeException("audioFile 읽기 실패", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<org.springframework.util.MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> res = restTemplate.exchange(
                aiServer + "/interview/evaluate",
                HttpMethod.POST,
                entity,
                Map.class
        );

        Map<String, Object> py = res.getBody();
        if (py == null) throw new RuntimeException("파이썬 평가 응답이 비었습니다.");

        int score = 0;
        Object scoreObj = py.get("score");
        if (scoreObj instanceof Number n) score = n.intValue();
        else score = Integer.parseInt(String.valueOf(scoreObj));

        String feedback = String.valueOf(py.get("feedback"));
        String transcript = String.valueOf(py.getOrDefault("transcript", ""));

        // 3. DB 저장
        InterviewResultDTO interviewResult = new InterviewResultDTO();
        interviewResult.setInterviewScore(String.valueOf(score));
        interviewResult.setInterviewReason(feedback);
        interviewResult.setCoverPostsIdx(interviewId);

        aiMapper.insertInterview(interviewResult);

        Map<String, Object> response = new HashMap<>();
        response.put("totalScore", score);
        response.put("feedback", feedback);
        return response;
    }
    public InterviewResultDTO getInterviewResult(long idinterviewIdx) {
        return aiMapper.selectInterview(idinterviewIdx);
    }



    // AI 기업 매칭
    public List<ComPostsDTO> getJobPostsByCompany(int companyIdx) {
        return aiMapper.jobPosts(companyIdx);
    }

    @Transactional
    public List<AIRecommendComDTO> recommendJobPosts(int compnayIdx, long jobPostsIdx, int topN) {
        System.out.println("===== [AI COMPANY MATCH START] =====");
        System.out.println("companyIdx = " + compnayIdx);
        System.out.println("jobPostsIdx = " + jobPostsIdx);
        System.out.println("topN = " + topN);

        Integer oCompanyIdx = aiMapper.selectCompanyIdx(jobPostsIdx);

        System.out.println("[CHECK] oCompanyIdx = " + oCompanyIdx);

        if (oCompanyIdx == null || !oCompanyIdx.equals(compnayIdx)) {
            return List.of();
        }

        JobPostsDTO jobPosts = aiMapper.selectJobPostsIdx(jobPostsIdx);

        System.out.println("[JOB_POSTS] = " + jobPosts);

        if(jobPosts == null) return List.of();

        List<CompanyCoverCandidateDTO> candidates = aiMapper.selectCandidates(jobPostsIdx);

        System.out.println("[CANDIDATES SIZE] = " + (candidates == null ? "null" : candidates.size()));
        if (candidates == null || candidates.isEmpty()) {
            System.out.println("❌ 지원자 없음 → AI 호출 안 함");
            return List.of();
        }


        int limit = (topN <= 0) ? 20 : Math.min(topN, 20);

        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> jobObj = new HashMap<>();
        jobObj.put("jobPostsIdx", jobPosts.getJobPostIdx());
        jobObj.put("title", jobPosts.getTitle());
        jobObj.put("techStack", jobPosts.getTechStack());
        jobObj.put("career", jobPosts.getCareer());
        jobObj.put("education", jobPosts.getEducation());
        jobObj.put("employmentType", jobPosts.getEmploymentType());
        jobObj.put("salary", jobPosts.getSalary());

        payload.put("job_post", jobObj);
        payload.put("topN", limit);

        List<Map<String, Object>> covers = new ArrayList<>();
        for(CompanyCoverCandidateDTO c : candidates) {

            System.out.println(" - candidate: " + c.getJobseekerName()
                    + ", coverPostsIdx=" + c.getCoverPostsIdx()
                    + ", applicantIdx=" + c.getCompanyApplicantIdx());

            Map<String, Object> map = new HashMap<>();
            map.put("companyApplicantIdx",c.getCompanyApplicantIdx());
            map.put("jobseekerIdx",c.getJobseekerIdx());
            map.put("coverPostsIdx",c.getCoverPostsIdx());
            map.put("jobseekerName",c.getJobseekerName());
            map.put("hopeJob", c.getHopeJob());
            map.put("hopeRegion",c.getHopeRegion());
            map.put("education",c.getEducation());
            map.put("career",c.getCareer());
            map.put("keySkill", c.getKeyskill());
            map.put("applyMotive",c.getApplyMotive());
            map.put("growthProcess",c.getGrowthProcess());
            map.put("personality",c.getPersonality());
            map.put("jobExperience",c.getJobExperience());

            covers.add(map);
        }
        payload.put("cover_posts", covers);
        System.out.println("===== [AI PAYLOAD] =====");
        System.out.println(payload);

        AIMatchDTO aiResult = callAI(payload);

        System.out.println("===== [AI RESPONSE RAW] =====");
        System.out.println(aiResult);

        if (aiResult == null) {
            System.out.println("❌ aiResult NULL");
            return List.of();
        }
        if (aiResult.getResults() == null) {
            System.out.println("❌ aiResult.results NULL");
            return List.of();
        }
        if (aiResult.getResults().isEmpty()) {
            System.out.println("⚠️ aiResult.results EMPTY");
            return List.of();
        }

        aiMapper.deleteCompanyMatching(compnayIdx, jobPostsIdx);

        System.out.println("[DELETE DONE]");

        List<AIRecommendComDTO> result = new  ArrayList<>();

        for(AIMatchDTO.ResultDTO r : aiResult.getResults()) {

            System.out.println("[AI RESULT ITEM] " + r);

            AIRecommendComDTO aiRecommendComDTO = new AIRecommendComDTO();
            aiRecommendComDTO.setCompanyIdx(compnayIdx);
            aiRecommendComDTO.setJobPostsIdx(jobPostsIdx);

            aiRecommendComDTO.setCompnayApplicantIdx(r.getCompnayApplicantIdx());

            aiRecommendComDTO.setJobseekersIdx(r.getJobseekerIdx());
            aiRecommendComDTO.setCoverPostsIdx(r.getCoverPostsIdx());
            aiRecommendComDTO.setName(r.getJobseekerName());
            aiRecommendComDTO.setMatchRate((int) Math.round(r.getScore()));
            aiRecommendComDTO.setReason(r.getReason());

            int inserted = aiMapper.insertCompanyMatching(aiRecommendComDTO);

            System.out.println(" → insert result = " + inserted);

            result.add(aiRecommendComDTO);
        }
        System.out.println("===== [AI COMPANY MATCH END] =====");
        return result;
    }

    private AIMatchDTO callAI(Map<String, Object> payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload,headers);

        ResponseEntity<AIMatchDTO> res = restTemplate.exchange(
                aiServer + "/match/company",
                HttpMethod.POST,
                entity,
                AIMatchDTO.class
        );
        return res.getBody();
    }
    public JobseekerResumeDetailDTO getLatestResume(int jobseekerIdx) {
        return aiMapper.selectLatestResumeByJobseeker(jobseekerIdx);
    }


}