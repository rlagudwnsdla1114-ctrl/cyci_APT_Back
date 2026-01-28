package kr.soft.apt.api.UserInfo;

import jakarta.servlet.http.HttpServletRequest;
import kr.soft.apt.dto.UserInfo.CompanyInfoDTO;
import kr.soft.apt.dto.UserInfo.JobseekerInfoDTO;
import kr.soft.apt.service.UserInfo.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserInfoContoller {

    @Autowired
    UserInfoService userInfoService;

    @GetMapping("/company/userinfo")
    public ResponseEntity<?> myCompanyInfo(HttpServletRequest request) {
        long idx = getUserIdx(request);


        CompanyInfoDTO dto = userInfoService.getCompanyInfo(idx);
        if (dto == null) return ResponseEntity.badRequest().body("회사 정보가 없습니다.");
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/jobseeker/userinfo")
    public ResponseEntity<?> myJobseekerInfo(HttpServletRequest request) {
        long idx = getUserIdx(request);


        JobseekerInfoDTO dto = userInfoService.getJobseekerInfo(idx);
        if (dto == null) return ResponseEntity.badRequest().body("구직자 정보가 없습니다.");
        return ResponseEntity.ok(dto);
    }

    private long getUserIdx(HttpServletRequest request) {
        Object v = request.getAttribute("userIdx");
        if (v == null) throw new RuntimeException("userIdx not found (AuthInterceptor 확인)");
        if (v instanceof Long) return (Long) v;
        return Long.parseLong(v.toString());
    }
}
