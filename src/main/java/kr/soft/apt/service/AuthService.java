package kr.soft.apt.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.soft.apt.config.jwt.JwtTokenProvider;
import kr.soft.apt.dto.Auth.TokenRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTokenService redisTokenService;

    // 쿠키 이름(원하는대로 바꿔도 됨)
    private static final String REFRESH_COOKIE = "refreshToken";

    public TokenRes refresh(HttpServletRequest request, HttpServletResponse response, String refreshTokenFromBody) {
        String refreshToken = StringUtils.hasText(refreshTokenFromBody)
                ? refreshTokenFromBody
                : readCookie(request, REFRESH_COOKIE);

        if (!StringUtils.hasText(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 없습니다.");
        }

        // 1) JWT 자체 유효성(서명/만료) 확인
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다.");
        }

        // 2) 토큰에서 userId/userIdx 추출
        String userId = jwtTokenProvider.getUserId(refreshToken);
        Long userIdx = jwtTokenProvider.getUserIdx(refreshToken);

        if (!StringUtils.hasText(userId) || userIdx == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰 정보가 올바르지 않습니다.");
        }

        // 3) Redis에 저장된 refresh와 일치하는지 확인(로그아웃/강제만료 대응)
        if (!redisTokenService.matchesRefreshToken(userId, refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었거나 로그아웃된 상태입니다.");
        }

        // 4) 새 Access 발급
        String newAccess = jwtTokenProvider.createAccessToken(userIdx, userId);
        long accessExp = jwtTokenProvider.getExpiration(newAccess);

        // (선택) Access도 Redis에 저장하고 싶으면
        redisTokenService.saveAccessToken(userId, newAccess);

        // 5) (권장) Refresh 회전(rotate): 새 refresh 발급 + Redis 갱신 + 쿠키 갱신
        String newRefresh = jwtTokenProvider.createRefreshToken(userIdx, userId);
        long refreshExp = jwtTokenProvider.getExpiration(newRefresh);

        redisTokenService.saveRefreshToken(userId, newRefresh);
        setRefreshCookie(response, newRefresh, (int) (refreshExp / 1000));

        return new TokenRes(newAccess, accessExp, null, null);
        // refreshToken을 응답 바디로도 보내고 싶으면:
        // return new TokenRes(newAccess, accessExp, newRefresh, refreshExp);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = resolveBearerToken(request);

        // access 없으면 그냥 refresh만 지우고 쿠키 삭제 처리
        String refreshToken = readCookie(request, REFRESH_COOKIE);

        if (StringUtils.hasText(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
            String userId = jwtTokenProvider.getUserId(refreshToken);
            if (StringUtils.hasText(userId)) {
                redisTokenService.deleteRefreshToken(userId);
                redisTokenService.deleteAccessTokenByUserId(userId); // 선택
            }
        }

        // access 블랙리스트(선택)
        if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
            long exp = jwtTokenProvider.getExpiration(accessToken);
            redisTokenService.blacklistAccessToken(accessToken, exp);
        }

        // 쿠키 제거
        clearRefreshCookie(response);
    }

    private String resolveBearerToken(HttpServletRequest request) {
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(auth)) return null;
        if (auth.startsWith("Bearer ")) return auth.substring(7);
        return null;
    }

    private String readCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (Cookie c : request.getCookies()) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }

    private void setRefreshCookie(HttpServletResponse response, String token, int maxAgeSeconds) {
        // localhost 개발 기준: secure(false). 배포 HTTPS면 secure(true)로 바꾸기.
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE, token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAgeSeconds)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearRefreshCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
