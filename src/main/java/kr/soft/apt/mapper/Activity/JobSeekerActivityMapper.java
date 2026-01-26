package kr.soft.apt.mapper.Activity;

import kr.soft.apt.dto.Activity.AppliedItemDTO;
import kr.soft.apt.dto.Activity.ScrappedItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobSeekerActivityMapper {

    String selectStatus(@Param("jobSeekerIdx") Long jobSeekerIdx,
                        @Param("jobPostsIdx") Long jobPostsIdx);

    int insertScrap(@Param("jobSeekerIdx") Long jobSeekerIdx,
                    @Param("jobPostsIdx") Long jobPostsIdx);

    int deleteScrap(@Param("jobSeekerIdx") Long jobSeekerIdx,
                    @Param("jobPostsIdx") Long jobPostsIdx);

    int insertApply(@Param("jobSeekerIdx") Long jobSeekerIdx,
                    @Param("jobPostsIdx") Long jobPostsIdx);

    int updateStatus(@Param("jobSeekerIdx") Long jobSeekerIdx,
                     @Param("jobPostsIdx") Long jobPostsIdx,
                     @Param("status") String status);

    int touchAppliedAt(@Param("jobSeekerIdx") Long jobSeekerIdx,
                       @Param("jobPostsIdx") Long jobPostsIdx);

    List<AppliedItemDTO> listApplied(@Param("jobSeekerIdx") Long jobSeekerIdx);

    List<ScrappedItemDTO> listScrapped(@Param("jobSeekerIdx") Long jobSeekerIdx);

    int deleteApplied(@Param("jobSeekerIdx") Long jobSeekerIdx,
                      @Param("jobPostsIdx") Long jobPostsIdx);
}
