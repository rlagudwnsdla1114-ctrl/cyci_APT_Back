package kr.soft.apt.mapper.AI;

import kr.soft.apt.dto.AI.AIInterview.CoverPostsDTO;
import kr.soft.apt.dto.AI.AIInterview.InterviewResultDTO;
import kr.soft.apt.dto.AI.AIMatch.AIRecommendedCompanyDTO;
import kr.soft.apt.dto.AI.AIMatch.CoverPosts;
import kr.soft.apt.dto.AI.AIMatch.JobPosts;
import kr.soft.apt.dto.AI.AIMatch.JobseekerMatchingInsertDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AIMapper {

    // 구직자 AI 매칭
    CoverPosts coverposts(@Param("jobseekerIdx") long jobseekerIdx);

    List<JobPosts> jobposts();

    int jobmatchdelete(@Param("jobseekerIdx") long jobseekerIdx);

    int jobmatchinsert(JobseekerMatchingInsertDTO dto);

    List<AIRecommendedCompanyDTO> jobmatchselect(@Param("jobseekerIdx") int  jobseekerIdx,
                                                 @Param("topN") int topN);

    Long selectLatestCoverPostsIdx(long jobseekerIdx);



    //구직자 AI 면접
    CoverPostsDTO interjobidx(long jobseekerIdx);

    CoverPostsDTO selectCoverByCoverPostsIdx(long coverPostsIdx);

    int insertInterview(InterviewResultDTO interviewResultDTO);

    InterviewResultDTO selectInterview(@Param("idinterviewIdx") long idinterviewIdx);
}
