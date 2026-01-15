package kr.soft.apt.mapper.SignUp;

import kr.soft.apt.dto.SignUp.JobSeekerUserDTO;
import kr.soft.apt.dto.SignUp.JobseekerLoginDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JobSeekerUserMapper {

    void signup(JobSeekerUserDTO dto);
    JobseekerLoginDTO login(String email);

}
