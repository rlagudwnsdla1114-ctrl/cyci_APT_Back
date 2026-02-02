package kr.soft.apt.mapper.AI;

import kr.soft.apt.dto.AI.AIComList.AIComListDTO;
import kr.soft.apt.dto.AI.AIComList.AIComTopDTO;
import kr.soft.apt.dto.AI.AIComList.CompanyTalentDTO;
import kr.soft.apt.dto.AI.AIComListTopDTO;
import kr.soft.apt.dto.AI.AIJobiInterview.InterviewHistoryDTO;
import kr.soft.apt.dto.AI.AIListTopDTO;
import kr.soft.apt.dto.AI.JobMatchSelect.SelectJobMatchDTO;
import kr.soft.apt.dto.AI.JobSeekerDashboardCountDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AIListMapper {
    List<SelectJobMatchDTO> selectJobMatch(long jobseekerIdx);



    Map<String, Object> selectTalentDetail(@Param("comMatchingIdx") long comMatchingIdx);

    Integer existsComMatching(@Param("comMatchingIdx") long comMatchingIdx);

    Map<String, Object> selectTalentDetailByJobseeker(
            @Param("jobseekerIdx") int jobseekerIdx,
            @Param("jobPostsIdx") Long jobPostsIdx
    );



    List<InterviewHistoryDTO> getInterviewHistory(@Param("jobseekerIdx") long jobseekerIdx);

    List<AIListTopDTO> selectJobMatchTop(long jobseekerIdx);

    int countMyJobPosts(@Param("companyIdx") int companyIdx);

    int countMyApplicants(@Param("companyIdx") int companyIdx);

    List<AIComTopDTO> selectLatestMatchingTop3(@Param("companyIdx") int companyIdx);

    List<CompanyTalentDTO> selectRecommendedTalentsTop3(@Param("companyIdx") int companyIdx);

    JobSeekerDashboardCountDTO selectDashboardCounts(@Param("jobseekerIdx") int jobseekerIdx,
                                                     @Param("interviewStatus") String interviewStatus);
}