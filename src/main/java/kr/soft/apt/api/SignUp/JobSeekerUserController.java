package kr.soft.apt.api.SignUp;


import kr.soft.apt.dto.SignUp.JobSeekerUserDTO;
import kr.soft.apt.dto.SignUp.MemberLoginDTO;
import kr.soft.apt.service.SignUp.JobSeekerUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/member")
@RestController

public class JobSeekerUserController {

    @Autowired
    private JobSeekerUserService jobSeekerUserService;

    @PostMapping("/jobseekerusersignup")
    public ResponseEntity<?> jbsignup(@RequestBody JobSeekerUserDTO jobSeekerUserDTO){
        log.info("/api/member/jobseekerusersignup/");
        log.info("data {}", jobSeekerUserDTO.toString());
        jobSeekerUserService.jbsignup(jobSeekerUserDTO);

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/jblogin")
    public ResponseEntity jblogin(@RequestBody MemberLoginDTO memberLoginDTO){
        log.info("/api/member/login");
        log.info("data {}", memberLoginDTO.toString());

        String check=jobSeekerUserService.jblogin(memberLoginDTO);
        return ResponseEntity.ok(check);
    }


}
