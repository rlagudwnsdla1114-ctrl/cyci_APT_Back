package kr.soft.apt.mapper;

import kr.soft.apt.dto.Employment.EmploymentListDTO;
import kr.soft.apt.dto.Employment.EmploymentReadDTO;
import kr.soft.apt.dto.Employment.EmploymentUpdateDTO;
import kr.soft.apt.dto.Employment.EmploymentWriteDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface EmploymentMapper {

    void writeEmployment(EmploymentWriteDTO employmentWriteDTO);

    EmploymentReadDTO readMyJobPost(@Param("jobPostsIdx") long jobPostsIdx,
                                    @Param("companyIdx") long companyIdx);

    int updateMyJobPost(EmploymentUpdateDTO dto);

    List<EmploymentListDTO> listMyJobPosts(long companyIdx);

}
