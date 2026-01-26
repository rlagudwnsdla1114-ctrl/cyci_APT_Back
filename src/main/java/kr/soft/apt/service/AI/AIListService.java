package kr.soft.apt.service.AI;

import kr.soft.apt.dto.AI.AIComList.AIComListDTO;
import kr.soft.apt.dto.AI.AIComList.CompanySummaryDTO;
import kr.soft.apt.dto.AI.AIJobiInterview.InterviewHistoryDTO;
import kr.soft.apt.dto.AI.AIListTopDTO;
import kr.soft.apt.dto.AI.JobMatchSelect.SelectJobMatchDTO;
import kr.soft.apt.mapper.AI.AIListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return aiListMapper.selectComMatch(jobPostsIdx);
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
}
