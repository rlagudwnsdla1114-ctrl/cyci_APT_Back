package kr.soft.apt.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.soft.apt.common.ApiResponse;
import kr.soft.apt.dto.Auth.RefreshReq;
import kr.soft.apt.dto.Auth.TokenRes;
import kr.soft.apt.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRes>> refresh(
            @RequestBody(required = false) RefreshReq req,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String rt = (req != null) ? req.getRefreshToken() : null;
        TokenRes tokenRes = authService.refresh(request, response, rt);
        return ApiResponse.success(tokenRes);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(request, response);
        return ApiResponse.success("ok");
    }
}
