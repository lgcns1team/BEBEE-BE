package com.lgcns.bebee.member.infrastructure.security;

import io.jsonwebtoken.Claims;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 리프레시 토큰 블랙리스트 관리 서비스
 * 로그아웃된 리프레시 토큰을 Redis에 저장하여 재사용을 방지합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenBlacklistService {

    private static final String BLACKLIST_PREFIX = "blacklist:refresh:";

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 리프레시 토큰을 블랙리스트에 추가합니다.
     * TTL은 토큰의 만료 시간까지로 설정됩니다.
     *
     * @param refreshToken 블랙리스트에 추가할 리프레시 토큰
     */
    public void addToBlacklist(String refreshToken) {
        try {
            Claims claims = jwtTokenProvider.parseClaims(refreshToken);
            long expirationTime = claims.getExpiration().getTime();
            long currentTime = System.currentTimeMillis();
            long ttlSeconds = (expirationTime - currentTime) / 1000;

            if (ttlSeconds > 0) {
                String key = BLACKLIST_PREFIX + refreshToken;
                redisTemplate.opsForValue().set(key, "blacklisted", Duration.ofSeconds(ttlSeconds));
                log.debug("리프레시 토큰이 블랙리스트에 추가되었습니다. TTL: {}초", ttlSeconds);
            }
        } catch (Exception e) {
            log.warn("블랙리스트 추가 실패: {}", e.getMessage());
            // 토큰 파싱 실패 시에도 예외를 던지지 않고 로그만 남김
        }
    }

    /**
     * 리프레시 토큰이 블랙리스트에 있는지 확인합니다.
     *
     * @param refreshToken 확인할 리프레시 토큰
     * @return 블랙리스트에 있으면 true, 없으면 false
     */
    public boolean isBlacklisted(String refreshToken) {
        String key = BLACKLIST_PREFIX + refreshToken;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}

