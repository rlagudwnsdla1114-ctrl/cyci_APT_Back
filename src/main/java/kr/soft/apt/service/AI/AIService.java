package kr.soft.apt.service.AI;

import kr.soft.apt.dto.AI.*;
import kr.soft.apt.mapper.AI.AIMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AIService {

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
                aiServer + "/match",
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

        return aiMapper.jobmatchselect((int) jobseekerIdx, safeTopN);
    }
}
