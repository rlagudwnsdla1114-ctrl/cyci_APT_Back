package kr.soft.apt.mapper.Employment;

import kr.soft.apt.dto.Employment.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface CompanyEmploymentMapper {

    void writeEmployment(EmploymentWriteDTO employmentWriteDTO);

    EmploymentReadDTO readMyJobPost(@Param("jobPostsIdx") long jobPostsIdx,
                                    @Param("companyIdx") long companyIdx);

    int updateMyJobPost(EmploymentUpdateDTO dto);

    List<EmploymentListDTO> listMyJobPosts(long companyIdx);

    int deleteMyJobPost(@Param("jobPostsIdx") Long jobPostsIdx,
                        @Param("companyIdx") Long companyIdx);

    AttachmentInfoDTO selectAttachmentInfoMy(@Param("jobPostsIdx") Long jobPostsIdx,
                                             @Param("companyIdx") Long companyIdx);

}
