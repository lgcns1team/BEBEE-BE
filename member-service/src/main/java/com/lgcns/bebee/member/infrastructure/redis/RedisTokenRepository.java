package com.lgcns.bebee.member.infrastructure.redis;

import com.lgcns.bebee.member.application.client.TokenRepository;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import java.time.Duration;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisTokenRepository implements TokenRepository {
    private static final String REFRESH_TOKEN_PREFIX = "RT";
    private static final String BLACKLIST_PREFIX = "BLACKLIST:";

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void saveRefreshToken(Long memberId, String refreshToken, Long refreshTokenExpiresIn) {
        String key = REFRESH_TOKEN_PREFIX + memberId;

        redisTemplate.opsForValue()
                .set(key, refreshToken, Duration.ofSeconds(refreshTokenExpiresIn));
    }

    @Override
    public Optional<String> findRefreshTokenByMemberId(Long memberId) {
        String key = REFRESH_TOKEN_PREFIX + memberId;
        String refreshToken = redisTemplate.opsForValue().get(key);

        return Optional.ofNullable(refreshToken);
    }

    @Override
    public void deleteRefreshToken(Long memberId) {
        String key = REFRESH_TOKEN_PREFIX + memberId;
        redisTemplate.delete(key);
    }

    /**
     * 액세스 토큰을 블랙리스트에 추가합니다.
     * 토큰의 남은 유효 시간만큼만 Redis에 저장됩니다.
     *
     * @param accessToken 블랙리스트에 추가할 액세스 토큰
     */
    @Override
    public void addToBlacklist(String accessToken) {
        try {
            Claims claims = jwtTokenProvider.parseClaims(accessToken);
            long expirationTime = claims.getExpiration().getTime();
            long currentTime = System.currentTimeMillis();
            long ttl = expirationTime - currentTime;

            // 토큰이 아직 유효한 경우에만 블랙리스트에 추가
            if (ttl > 0) {
                String key = BLACKLIST_PREFIX + accessToken;
                redisTemplate.opsForValue().set(key, "blacklisted", Duration.ofMillis(ttl));
                log.debug("액세스 토큰이 블랙리스트에 추가되었습니다. TTL: {}ms", ttl);
            } else {
                log.debug("이미 만료된 토큰이므로 블랙리스트에 추가하지 않습니다.");
            }
        } catch (Exception e) {
            log.warn("토큰 블랙리스트 추가 실패: {}", e.getMessage());
        }
    }

    /**
     * 액세스 토큰이 블랙리스트에 있는지 확인합니다.
     *
     * @param accessToken 확인할 액세스 토큰
     * @return 블랙리스트에 있으면 true, 없으면 false
     */
    public boolean isBlacklisted(String accessToken) {
        String key = BLACKLIST_PREFIX + accessToken;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}

