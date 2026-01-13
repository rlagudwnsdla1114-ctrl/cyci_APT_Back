package kr.soft.apt.mapper.SignUp;

import kr.soft.apt.dto.SignUp.JobSeekerUserDTO;
import kr.soft.apt.dto.SignUp.MemberLoginDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JobSeekerUserMapper {

    void jbsignup(JobSeekerUserDTO dto);
    MemberLoginDTO jblogin(String email);

}
