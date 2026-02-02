package kr.soft.apt.service.AI;

import kr.soft.apt.dto.AI.AIComList.AIComListDTO;
import kr.soft.apt.dto.AI.AIComList.CompanySummaryDTO;
import kr.soft.apt.dto.AI.AIJobiInterview.InterviewHistoryDTO;
import kr.soft.apt.dto.AI.AIListTopDTO;
import kr.soft.apt.dto.AI.JobMatchSelect.SelectJobMatchDTO;
import kr.soft.apt.dto.AI.JobSeekerDashboardCountDTO;
import kr.soft.apt.mapper.AI.AIListMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class AIListService {
    @Autowired
    private AIListMapper aiListMapper;

    public List<SelectJobMatchDTO> selectJobMatch(long jobseekerIdx) {
        List<SelectJobMatchDTO> mapperResult = aiListMapper.selectJobMatch(jobseekerIdx);
        System.out.println("mapper result = " + mapperResult);
        return mapperResult;
    }




    public Map<String, Object> getTalentDetail(long comMatchingIdx) {
        System.out.println("[SERVICE] getTalentDetail START comMatchingIdx=" + comMatchingIdx);
        log.info("[SERVICE] getTalentDetail START comMatchingIdx={}", comMatchingIdx);

        int exists = aiListMapper.existsComMatching(comMatchingIdx);
        System.out.println("[SERVICE] existsComMatching=" + exists);
        log.info("[SERVICE] existsComMatching={}", exists);

        Map<String, Object> detail = aiListMapper.selectTalentDetail(comMatchingIdx);
        System.out.println("[SERVICE] selectTalentDetail RESULT detail=" + detail);
        log.info("[SERVICE] selectTalentDetail RESULT detail={}", detail);

        if (detail == null) {
            detail = new HashMap<>();
            detail.put("comMatchingIdx", comMatchingIdx);
            detail.put("apply", "");
            return detail;
        }

        detail.putIfAbsent("apply", "");
        return detail;
    }

    public Map<String, Object> getTalentDetailByJobseeker(int jobseekerIdx, Long jobPostsIdx) {
        System.out.println("[SERVICE] getTalentDetailByJobseeker START jobseekerIdx=" + jobseekerIdx + ", jobPostsIdx=" + jobPostsIdx);
        log.info("[SERVICE] getTalentDetailByJobseeker START jobseekerIdx={}, jobPostsIdx={}", jobseekerIdx, jobPostsIdx);

        Map<String, Object> detail = aiListMapper.selectTalentDetailByJobseeker(jobseekerIdx, jobPostsIdx);
        System.out.println("[SERVICE] selectTalentDetailByJobseeker RESULT detail=" + detail);
        log.info("[SERVICE] selectTalentDetailByJobseeker RESULT detail={}", detail);

        if (detail == null) {
            detail = new HashMap<>();
            detail.put("jobseekerIdx", jobseekerIdx);
            detail.put("apply", "");
            return detail;
        }

        detail.putIfAbsent("apply", "");
        return detail;
    }






    public List<InterviewHistoryDTO> getInterviewHistory(long jobseekerIdx) {
        return aiListMapper.getInterviewHistory(jobseekerIdx);
    }

    public List<AIListTopDTO> selectJobMatchTop(long jobseekerIdx) {
        return aiListMapper.selectJobMatchTop(jobseekerIdx);
    }

    public CompanySummaryDTO getCompanyDashboardSummary(int companyIdx) {
        CompanySummaryDTO companyDashboardSummaryDTO = new CompanySummaryDTO();

        companyDashboardSummaryDTO.setPostCount(aiListMapper.countMyJobPosts(companyIdx));
        companyDashboardSummaryDTO.setApplicantCount(aiListMapper.countMyApplicants(companyIdx));
        companyDashboardSummaryDTO.setTop3(aiListMapper.selectLatestMatchingTop3(companyIdx));
        companyDashboardSummaryDTO.setRecommendedTalents(aiListMapper.selectRecommendedTalentsTop3(companyIdx));

        return companyDashboardSummaryDTO;
    }

    public JobSeekerDashboardCountDTO getCounts(int jobseekerIdx) {
        String interviewStatus = "검토중";
        return aiListMapper.selectDashboardCounts(jobseekerIdx, interviewStatus);
    }
}
