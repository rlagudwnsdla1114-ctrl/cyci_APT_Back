package kr.soft.apt.mapper.AI;

import kr.soft.apt.dto.AI.AIComList.AIComListDTO;
import kr.soft.apt.dto.AI.AIComList.AIComTopDTO;
import kr.soft.apt.dto.AI.AIComListTopDTO;
import kr.soft.apt.dto.AI.AIJobiInterview.InterviewHistoryDTO;
import kr.soft.apt.dto.AI.AIListTopDTO;
import kr.soft.apt.dto.AI.JobMatchSelect.SelectJobMatchDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AIListMapper {
    List<SelectJobMatchDTO> selectJobMatch(long jobseekerIdx);

    List<AIComListDTO> selectComMatch(@Param("jobPostsIdx") long jobPostsIdx);

    List<InterviewHistoryDTO> getInterviewHistory(@Param("jobseekerIdx") long jobseekerIdx);

    List<AIListTopDTO> selectJobMatchTop(long jobseekerIdx);

    int countMyJobPosts(@Param("companyIdx") int companyIdx);

    int countMyApplicants(@Param("companyIdx") int companyIdx);

    Long selectLatestMatchedJobPostsIdx(@Param("companyIdx") int companyIdx);

    List<AIComTopDTO> selectComMatchTop3ByJobPostsIdx(@Param("jobPostsIdx") long jobPostsIdx);

    List<AIComListDTO> selectRecentComMatches(@Param("companyIdx") int companyIdx, @Param("limit") int limit);
}