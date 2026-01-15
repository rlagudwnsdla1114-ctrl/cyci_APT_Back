package kr.soft.apt.service.SignUp;


import kr.soft.apt.dto.SignUp.JobSeekerUserDTO;
import kr.soft.apt.dto.SignUp.MemberLoginDTO;
import kr.soft.apt.mapper.SignUp.JobSeekerUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JobSeekerUserService {

    @Autowired
    private JobSeekerUserMapper jobSeekerUserMapper;


    public void jbsignup(JobSeekerUserDTO dto){

        jobSeekerUserMapper.jbsignup(dto);





    }

    public String jblogin(MemberLoginDTO dto){
        MemberLoginDTO resultDTO=jobSeekerUserMapper.jblogin(dto.getEmail());
        if(resultDTO==null || !resultDTO.getPassword().equals(dto.getPassword())){
            return  null;
        }
        String text="apple_"+dto.getEmail();
        return text;
    }


}
