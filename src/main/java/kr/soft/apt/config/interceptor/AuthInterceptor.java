package kr.soft.apt.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.soft.apt.config.jwt.JwtTokenProvider;
import kr.soft.apt.service.RedisTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTokenService redisTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String authHeader = request.getHeader("Authorization");

        log.info("authHeader: " + authHeader);

        // ✅ 1. 헤더에 토큰이 없을 때
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("❌ Authorization NOT HEAD.");
            return false;
        }

        String token = authHeader.substring(7);

        // ✅ 2. JWT 자체 유효성 검증
        if (!jwtTokenProvider.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("❌ 유효하지 않은 JWT 토큰입니다.");
            return false;
        }

        String userId = jwtTokenProvider.getUserId(token);
        long idx = jwtTokenProvider.getUserIdx(token);

        String uri = request.getRequestURI();

        String companyKey = "company:" + idx;
        String jobseekerKey = "jobseeker:" + idx;

        String redisKey;

        if (uri.startsWith("/api/company")) {
            redisKey = companyKey;
        } else if (uri.startsWith("/api/jobseeker")) {
            redisKey = jobseekerKey;
        } else {
            // ✅ /api/user 같은 공통 API는 둘 다 확인
            if (redisTokenService.existsAccessToken(companyKey, token)) {
                redisKey = companyKey;
            } else if (redisTokenService.existsAccessToken(jobseekerKey, token)) {
                redisKey = jobseekerKey;
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("⚠️ Redis에 등록되지 않은 토큰입니다.");
                return false;
            }
        }

        log.info("test");

        request.setAttribute("userId", userId);
        request.setAttribute("userIdx", idx);
        request.setAttribute("redisKey", redisKey);

        // ✅ 4. TTL 갱신 (30분)
        redisTokenService.refreshAccessTokenTTL(redisKey);

        return true;
    }
}