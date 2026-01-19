package kr.soft.apt.mapper.Employment;

import kr.soft.apt.dto.Employment.AttachmentInfoDTO;


import kr.soft.apt.dto.Employment.EmploymentPublicListDTO;
import kr.soft.apt.dto.Employment.EmploymentPublicReadDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobSeekerEmploymentMapper {

    List<EmploymentPublicListDTO> listPublicJobPosts();

    EmploymentPublicReadDTO readPublicJobPost(@Param("jobPostsIdx") Long jobPostsIdx);

    int increaseViewCount(@Param("jobPostsIdx") Long jobPostsIdx);

    AttachmentInfoDTO selectAttachmentInfoPublic(@Param("jobPostsIdx") Long jobPostsIdx);
}
