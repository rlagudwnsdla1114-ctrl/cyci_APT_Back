package kr.soft.apt.mapper.UserInfo;

import kr.soft.apt.dto.UserInfo.CompanyInfoDTO;
import kr.soft.apt.dto.UserInfo.JobseekerInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper {


    CompanyInfoDTO CompanyInfo(@Param("companyIdx") long companyIdx);


    JobseekerInfoDTO JobseekerInfo(@Param("jobseekerIdx") long jobseekerIdx);
}
