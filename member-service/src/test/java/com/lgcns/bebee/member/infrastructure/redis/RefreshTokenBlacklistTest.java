package com.lgcns.bebee.member.infrastructure.redis;

import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

/**
 * RefreshTokenBlacklist Unit Test
 * 
 * 테스트 범위: 블랙리스트 추가/조회 로직
 * Mock 대상: RedisTemplate, JwtTokenProvider
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenBlacklist 단위 테스트")
class RefreshTokenBlacklistTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RefreshTokenBlacklist refreshTokenBlacklist;

    private String validRefreshToken;
    private Claims validClaims;

    @BeforeEach
    void setUp() {
        validRefreshToken = "validRefreshToken";
    }

    @Nested
    @DisplayName("addToBlacklist() 메서드")
    class AddToBlacklistTest {

        @Test
        @DisplayName("정상적인 토큰을 블랙리스트에 추가한다")
        void addToBlacklist_success() {
            // given
            Claims claims = mock(Claims.class);
            given(claims.getExpiration()).willReturn(new Date(System.currentTimeMillis() + 1209600000L));
            given(jwtTokenProvider.parseClaims(validRefreshToken)).willReturn(claims);
            given(redisTemplate.opsForValue()).willReturn(valueOperations);

            // when
            refreshTokenBlacklist.addToBlacklist(validRefreshToken);

            // then
            verify(jwtTokenProvider).parseClaims(validRefreshToken);
            verify(redisTemplate).opsForValue();
            verify(valueOperations).set(eq("blacklist:refresh:" + validRefreshToken), eq("blacklisted"), any(Duration.class));
        }

        @Test
        @DisplayName("만료된 토큰은 블랙리스트에 추가하지 않는다")
        void addToBlacklist_expiredToken() {
            // given
            Claims expiredClaims = mock(Claims.class);
            given(expiredClaims.getExpiration()).willReturn(new Date(System.currentTimeMillis() - 1000L)); // 과거

            given(jwtTokenProvider.parseClaims(validRefreshToken)).willReturn(expiredClaims);

            // when
            refreshTokenBlacklist.addToBlacklist(validRefreshToken);

            // then
            verify(jwtTokenProvider).parseClaims(validRefreshToken);
            verify(redisTemplate, never()).opsForValue();
        }

        @Test
        @DisplayName("유효하지 않은 토큰은 예외 없이 처리한다")
        void addToBlacklist_invalidToken() {
            // given
            given(jwtTokenProvider.parseClaims(validRefreshToken))
                    .willThrow(new RuntimeException("Invalid token"));

            // when & then
            assertThatCode(() -> refreshTokenBlacklist.addToBlacklist(validRefreshToken))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("isBlacklisted() 메서드")
    class IsBlacklistedTest {

        @Test
        @DisplayName("블랙리스트에 있는 토큰은 true를 반환한다")
        void isBlacklisted_true() {
            // given
            given(redisTemplate.hasKey("blacklist:refresh:" + validRefreshToken)).willReturn(Boolean.TRUE);

            // when
            boolean result = refreshTokenBlacklist.isBlacklisted(validRefreshToken);

            // then
            assertThat(result).isTrue();
            verify(redisTemplate).hasKey("blacklist:refresh:" + validRefreshToken);
        }

        @Test
        @DisplayName("블랙리스트에 없는 토큰은 false를 반환한다")
        void isBlacklisted_false() {
            // given
            given(redisTemplate.hasKey("blacklist:refresh:" + validRefreshToken)).willReturn(Boolean.FALSE);

            // when
            boolean result = refreshTokenBlacklist.isBlacklisted(validRefreshToken);

            // then
            assertThat(result).isFalse();
            verify(redisTemplate).hasKey("blacklist:refresh:" + validRefreshToken);
        }
    }
}

