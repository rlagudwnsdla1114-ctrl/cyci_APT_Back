package kr.soft.apt.api.SignUp;

import kr.soft.apt.dto.SignUp.CompanyUserDTO;
import kr.soft.apt.dto.SignUp.MemberLoginDTO;
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
@RequestMapping("/api/member")

public class CompanyUserController {

    @Autowired
    private CompanyUserService companyUserService;

    @PostMapping("/companyusersignup")
    public ResponseEntity<?> signup(@RequestBody CompanyUserDTO companyUserDTO){
        log.info("/api/member/companyusersignup/");
        log.info("data {}", companyUserDTO.toString());
        companyUserService.signup(companyUserDTO);

    return ResponseEntity.ok("ok");
    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberLoginDTO memberLoginDTO){
        log.info("/api/member/login");
        log.info("data {}", memberLoginDTO.toString());

        String check=companyUserService.login(memberLoginDTO);
        return ResponseEntity.ok(check);
    }

}
