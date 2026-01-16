package kr.soft.apt.api.SignUp;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.common.ApiResponse;
import kr.soft.apt.dto.SignUp.CompanyLoginDTO;
import kr.soft.apt.dto.SignUp.CompanyUserDTO;
import kr.soft.apt.dto.SignUp.JobseekerLoginDTO;
import kr.soft.apt.service.RedisTokenService;
import kr.soft.apt.service.SignUp.CompanyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/company")

public class CompanyUserController {

    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private RedisTokenService redisTokenService;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody CompanyUserDTO companyUserDTO){
        log.info("/api/company/signup/");
        log.info("data {}", companyUserDTO.toString());
        companyUserService.signup(companyUserDTO);

    return ResponseEntity.ok("ok");
    }


    @PostMapping("/login")
    public  ResponseEntity<ApiResponse<String>> login(@RequestBody  CompanyLoginDTO companyLoginDTO){

        return ApiResponse.success(companyUserService.login(companyLoginDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        String redisKey = (String) request.getAttribute("redisKey"); // company:idx
        String userId = (String) request.getAttribute("userId");

        if (redisKey != null) redisTokenService.deleteAccessToken(redisKey);
        if (userId != null) redisTokenService.deleteRefreshToken(userId);

        return ResponseEntity.ok("company-logout-ok");
    }

}
