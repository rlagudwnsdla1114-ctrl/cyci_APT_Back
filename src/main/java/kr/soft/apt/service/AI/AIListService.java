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
import org.springframework.stereotype.Service;

import java.util.List;
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


    public List<AIComListDTO> selectComMatch(long jobPostsIdx) {

        List<AIComListDTO> resultList = aiListMapper.selectComMatch(jobPostsIdx);

        System.out.println("===== selectComMatch 결과 확인 =====");

        if (resultList == null) {
            System.out.println("resultList 자체가 null");
        } else if (resultList.isEmpty()) {
            System.out.println("resultList 비어 있음");
        } else {
            AIComListDTO dto = resultList.get(0);
            System.out.println("jobSeekerIdx = " + dto.getJobSeekerIdx());
            System.out.println("name         = " + dto.getName());
            System.out.println("matchRate    = " + dto.getMatchRate());
            System.out.println("matchDate    = " + dto.getMatchDate());
            System.out.println("keySkill     = " + dto.getKeySkill());
            System.out.println("comAiReason  = " + dto.getComAiReason());
        }

        return resultList;
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
