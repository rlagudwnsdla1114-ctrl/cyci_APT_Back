package kr.soft.apt.api.SignUp;


import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.common.ApiResponse;
import kr.soft.apt.dto.SignUp.JobSeekerUserDTO;
import kr.soft.apt.dto.SignUp.JobseekerLoginDTO;
import kr.soft.apt.service.RedisTokenService;
import kr.soft.apt.service.SignUp.JobSeekerUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/jobseeker")
@RestController

public class JobSeekerUserController {

    @Autowired
    private JobSeekerUserService jobSeekerUserService;

    @Autowired
    private RedisTokenService redisTokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody JobSeekerUserDTO jobSeekerUserDTO){
        log.info("/api/jobseeker/signup");
        log.info("data {}", jobSeekerUserDTO.toString());
        jobSeekerUserService.signup(jobSeekerUserDTO);

        return ResponseEntity.ok("ok");
    }


    @PostMapping("/login")
    public  ResponseEntity<ApiResponse<String>> login(@RequestBody JobseekerLoginDTO jobseekerLoginDTO){

        return ApiResponse.success(jobSeekerUserService.login(jobseekerLoginDTO));
    }



}
