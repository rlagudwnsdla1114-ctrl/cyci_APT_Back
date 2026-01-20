package kr.soft.apt.mapper.EditUser;

import kr.soft.apt.dto.EditUser.EditCompanyUserDTO;
import kr.soft.apt.dto.EditUser.EditJobseekerUserDTO;
import kr.soft.apt.dto.EditUser.GetCompanyUserDTO;
import kr.soft.apt.dto.EditUser.GetJobseekerUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EditUserMapper {
    int editCompanyUser(EditCompanyUserDTO dto);

    GetCompanyUserDTO getCompanyUser(@Param("cIdx") long cIdx);

    int editJobseekerUser(EditJobseekerUserDTO dto);

    GetJobseekerUserDTO getJobseekerUser(@Param("jIdx") long jIdx);

}