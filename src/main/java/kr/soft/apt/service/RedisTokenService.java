package kr.soft.apt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    //REDIS 저장 시간
    final long ACCESSEXP = 1000L * 60 * 30; // 30분
    final long REFRESHEXP = 1000L * 60 * 60 * 24 * 14; // 14일 예시


    private final RedisTemplate<String, String> redisTemplate;

    // ✅ AccessToken 저장 (선택)
    // 실무에서는 보통 RefreshToken만 저장하지만, 검증 편의상 추가 가능
    public void saveAccessToken(String userId, String token) {
        redisTemplate.opsForValue().set("access:" + userId, token, ACCESSEXP, TimeUnit.MILLISECONDS);
    }

    // ✅ Access Token 존재 여부 확인
    public boolean existsAccessToken(String userId, String token) {
        String saved = redisTemplate.opsForValue().get("access:" + userId);
        return saved != null && saved.equals(token);
    }

    // ✅ TTL 갱신 (시간 초기화)
    public boolean refreshAccessTokenTTL(String userId) {
        String key = "access:" + userId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.expire(key, ACCESSEXP, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }

    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set("refresh:" + userId, refreshToken, REFRESHEXP, TimeUnit.MILLISECONDS);
    }

    public boolean matchesRefreshToken(String userId, String refreshToken) {
        String saved = getRefreshToken(userId);
        return saved != null && saved.equals(refreshToken);
    }

    public void deleteAccessTokenByUserId(String userId) {
        redisTemplate.delete("access:" + userId);
    }

    // ✅ Refresh Token 조회
    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get("refresh:" + userId);
    }

    // ✅ Access Token 삭제 (로그아웃/강제무효화)
    public void deleteAccessToken(String redisKey) {
        redisTemplate.delete(redisKey);
    }

    // ✅ Refresh Token 삭제 (로그아웃 시)
    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("refresh:" + userId);
    }

    // ✅ Access Token 블랙리스트 등록
    public void blacklistAccessToken(String accessToken, long expirationMillis) {
        redisTemplate.opsForValue().set(
                "blacklist:" + accessToken,
                "logout",
                expirationMillis,
                TimeUnit.MILLISECONDS
        );
    }

    // ✅ Access Token이 블랙리스트에 있는지 확인
    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey("blacklist:" + accessToken);
    }



}
