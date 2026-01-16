package kr.soft.apt.mapper.AI;

import kr.soft.apt.dto.AI.AIRecommendedCompanyDTO;
import kr.soft.apt.dto.AI.CoverPosts;
import kr.soft.apt.dto.AI.JobPosts;
import kr.soft.apt.dto.AI.JobseekerMatchingInsertDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AIMapper {

    CoverPosts coverposts(@Param("jobseekerIdx") long jobseekerIdx);

    List<JobPosts> jobposts();

    int jobmatchdelete(@Param("jobseekerIdx") long jobseekerIdx);

    int jobmatchinsert(JobseekerMatchingInsertDTO dto);

    List<AIRecommendedCompanyDTO> jobmatchselect(@Param("jobseekerIdx") int  jobseekerIdx,
                                                 @Param("topN") int topN);

    Long selectLatestCoverPostsIdx(long jobseekerIdx);
}
