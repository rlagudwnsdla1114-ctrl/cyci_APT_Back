package kr.soft.apt.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long accessTokenValidity = 1000L * 60 * 30;   // 30분
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 14; // 14일

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // ✅ Access Token 생성

    /***
     *
     * @param userIdx PK 검사용
     * @param userId  userId ( NickName, 화면 페이지 보여주는 용 )
     * @return
     */
    public String createAccessToken(Long userIdx, String userId) {
        return createToken(userIdx, userId, accessTokenValidity);
    }

    // ✅ Refresh Token 생성
//    public String createRefreshToken(Long userIdx, String userId) {
//        return createToken(userIdx, userId, refreshTokenValidity);
//    }

    // 내부 공통 메서드
    private String createToken(Long userIdx, String userId, long validityMillis) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMillis);

        return Jwts.builder()
                .setSubject(String.valueOf(userIdx)) // PK 기준
                .addClaims(Map.of("userId", userId)) // 부가정보
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("JWT 검증 실패: " + e.getMessage());
            return false;
        }
    }

    // ✅ 토큰 만료시간 조회
    public long getExpiration(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    // ✅ 토큰에서 userIdx 추출
    public Long getUserIdx(String token) {
        String subject = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.valueOf(subject);
    }

    // ✅ 토큰에서 userId 추출
    public String getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get("userId");
    }
}
