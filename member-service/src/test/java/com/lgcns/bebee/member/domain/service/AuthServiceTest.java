package com.lgcns.bebee.member.domain.service;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.member.core.exception.MemberErrors;
import com.lgcns.bebee.member.core.exception.MemberException;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.vo.Gender;
import com.lgcns.bebee.member.domain.entity.vo.MemberStatus;
import com.lgcns.bebee.member.domain.entity.vo.Role;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider.TokenPair;
import com.lgcns.bebee.member.infrastructure.redis.RedisTokenRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

/**
 * AuthService Unit Test
 * 
 * 테스트 범위: 로그인/회원가입/재발급/로그아웃 비즈니스 로직
 * Mock 대상: MemberRepository, PasswordEncoder, JwtTokenProvider, RefreshTokenBlacklistService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 단위 테스트")
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberCommandService memberCommandService;

    @Mock
    private PasswordPolicyValidator passwordPolicyValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RedisTokenRepository refreshTokenBlacklistService;

    @InjectMocks
    private AuthService authService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.create(
                "test@example.com",
                "encodedPassword",
                "홍길동",
                "길동이",
                LocalDate.of(1990, 1, 1),
                Gender.MALE,
                "010-1234-5678",
                Role.DISABLED,
                MemberStatus.ACTIVE,
                "서울시 강남구",
                BigDecimal.valueOf(37.5665),
                BigDecimal.valueOf(126.9780),
                "1168010100"
        );
    }

    @Nested
    @DisplayName("signup() 메서드")
    class SignupTest {

        @Test
        @DisplayName("정상적인 회원가입 시 토큰을 반환한다")
        void signup_success() {
            // given
            AuthService.SignupCommand command = new AuthService.SignupCommand(
                    "test@example.com",
                    "Test1234!@",
                    "홍길동",
                    "길동이",
                    LocalDate.of(1990, 1, 1),
                    "MALE",
                    "010-1234-5678",
                    "DISABLED",
                    "서울시 강남구",
                    37.5665,
                    126.9780,
                    "1168010100"
            );

            TokenPair expectedTokens = new TokenPair(
                    "accessToken",
                    "refreshToken",
                    java.time.Instant.now().plusSeconds(3600)
            );

            willDoNothing().given(passwordPolicyValidator).validate(anyString());
            given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
            given(memberCommandService.registerMember(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                    .willReturn(testMember);
            given(jwtTokenProvider.generateTokens(any(Member.class))).willReturn(expectedTokens);

            // when
            TokenPair result = authService.signup(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.accessToken()).isEqualTo("accessToken");
            assertThat(result.refreshToken()).isEqualTo("refreshToken");
            verify(passwordPolicyValidator).validate(command.password());
            verify(passwordEncoder).encode(command.password());
            verify(memberCommandService).registerMember(
                    eq(command.email()),
                    eq("encodedPassword"),
                    eq(command.name()),
                    eq(command.nickname()),
                    eq(command.birthDate()),
                    eq(Gender.MALE),
                    eq(Role.DISABLED),
                    eq(command.phoneNumber()),
                    eq(command.addressRoad()),
                    eq(command.latitude()),
                    eq(command.longitude()),
                    eq(command.districtCode())
            );
        }

        @Test
        @DisplayName("비밀번호 정책 위반 시 예외를 발생시킨다")
        void signup_invalidPassword() {
            // given
            AuthService.SignupCommand command = new AuthService.SignupCommand(
                    "test@example.com",
                    "weak",
                    "홍길동",
                    "길동이",
                    LocalDate.of(1990, 1, 1),
                    "MALE",
                    "010-1234-5678",
                    "DISABLED",
                    "서울시 강남구",
                    37.5665,
                    126.9780,
                    "1168010100"
            );

            willThrow(InvalidParamException.class)
                    .given(passwordPolicyValidator).validate(anyString());

            // when & then
            assertThatThrownBy(() -> authService.signup(command))
                    .isInstanceOf(InvalidParamException.class);
            verify(memberCommandService, never()).registerMember(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        }

        @Test
        @DisplayName("ADMIN 역할로 회원가입 시 예외를 발생시킨다")
        void signup_adminRole() {
            // given
            AuthService.SignupCommand command = new AuthService.SignupCommand(
                    "test@example.com",
                    "Test1234!@",
                    "홍길동",
                    "길동이",
                    LocalDate.of(1990, 1, 1),
                    "MALE",
                    "010-1234-5678",
                    "ADMIN",
                    "서울시 강남구",
                    37.5665,
                    126.9780,
                    "1168010100"
            );

            // when & then
            // parseRole에서 예외가 발생하므로 passwordPolicyValidator, passwordEncoder는 호출되지 않음
            assertThatThrownBy(() -> authService.signup(command))
                    .isInstanceOf(InvalidParamException.class);
            verify(passwordPolicyValidator, never()).validate(anyString());
            verify(passwordEncoder, never()).encode(anyString());
            verify(memberCommandService, never()).registerMember(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("login() 메서드")
    class LoginTest {

        @Test
        @DisplayName("정상적인 로그인 시 토큰을 반환한다")
        void login_success() {
            // given
            AuthService.LoginCommand command = new AuthService.LoginCommand(
                    "test@example.com",
                    "Test1234!@"
            );

            TokenPair expectedTokens = new TokenPair(
                    "accessToken",
                    "refreshToken",
                    java.time.Instant.now().plusSeconds(3600)
            );

            given(memberRepository.findByEmail(command.email())).willReturn(Optional.of(testMember));
            given(passwordEncoder.matches(command.password(), testMember.getPassword())).willReturn(true);
            given(jwtTokenProvider.generateTokens(testMember)).willReturn(expectedTokens);

            // when
            TokenPair result = authService.login(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.accessToken()).isEqualTo("accessToken");
            verify(memberRepository).findByEmail(command.email());
            verify(passwordEncoder).matches(command.password(), testMember.getPassword());
        }

        @Test
        @DisplayName("존재하지 않는 이메일로 로그인 시 예외를 발생시킨다")
        void login_invalidEmail() {
            // given
            AuthService.LoginCommand command = new AuthService.LoginCommand(
                    "nonexistent@example.com",
                    "Test1234!@"
            );

            given(memberRepository.findByEmail(command.email())).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> authService.login(command))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrors.INVALID_CREDENTIALS.getMessage());
            verify(passwordEncoder, never()).matches(anyString(), anyString());
        }

        @Test
        @DisplayName("잘못된 비밀번호로 로그인 시 예외를 발생시킨다")
        void login_invalidPassword() {
            // given
            AuthService.LoginCommand command = new AuthService.LoginCommand(
                    "test@example.com",
                    "WrongPassword"
            );

            given(memberRepository.findByEmail(command.email())).willReturn(Optional.of(testMember));
            given(passwordEncoder.matches(command.password(), testMember.getPassword())).willReturn(false);

            // when & then
            assertThatThrownBy(() -> authService.login(command))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrors.INVALID_CREDENTIALS.getMessage());
        }

        @Test
        @DisplayName("REJECTED 상태 회원 로그인 시 예외를 발생시킨다")
        void login_rejectedMember() {
            // given
            Member rejectedMember = Member.create(
                    "test@example.com",
                    "encodedPassword",
                    "홍길동",
                    "길동이",
                    LocalDate.of(1990, 1, 1),
                    Gender.MALE,
                    "010-1234-5678",
                    Role.DISABLED,
                    MemberStatus.REJECTED,
                    "서울시 강남구",
                    BigDecimal.valueOf(37.5665),
                    BigDecimal.valueOf(126.9780),
                    "1168010100"
            );

            AuthService.LoginCommand command = new AuthService.LoginCommand(
                    "test@example.com",
                    "Test1234!@"
            );

            given(memberRepository.findByEmail(command.email())).willReturn(Optional.of(rejectedMember));
            given(passwordEncoder.matches(command.password(), rejectedMember.getPassword())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> authService.login(command))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrors.MEMBER_STATUS_REJECTED.getMessage());
        }
    }

    @Nested
    @DisplayName("reissue() 메서드")
    class ReissueTest {

        @Test
        @DisplayName("정상적인 토큰 재발급 시 새 토큰을 반환한다")
        void reissue_success() {
            // given
            String refreshToken = "validRefreshToken";
            TokenPair expectedTokens = new TokenPair(
                    "newAccessToken",
                    "newRefreshToken",
                    java.time.Instant.now().plusSeconds(3600)
            );

            Claims claims = mock(Claims.class);
            given(claims.getSubject()).willReturn("test@example.com");

            given(refreshTokenBlacklistService.isBlacklisted(refreshToken)).willReturn(false);
            given(jwtTokenProvider.parseClaims(refreshToken)).willReturn(claims);
            given(memberRepository.findByEmail("test@example.com")).willReturn(Optional.of(testMember));
            given(jwtTokenProvider.reissueTokens(testMember, refreshToken)).willReturn(expectedTokens);

            // when
            TokenPair result = authService.reissue(refreshToken);

            // then
            assertThat(result).isNotNull();
            assertThat(result.accessToken()).isEqualTo("newAccessToken");
            verify(refreshTokenBlacklistService).isBlacklisted(refreshToken);
            verify(jwtTokenProvider).parseClaims(refreshToken);
        }

        @Test
        @DisplayName("블랙리스트에 있는 토큰으로 재발급 시 예외를 발생시킨다")
        void reissue_blacklistedToken() {
            // given
            String refreshToken = "blacklistedToken";

            given(refreshTokenBlacklistService.isBlacklisted(refreshToken)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> authService.reissue(refreshToken))
                    .isInstanceOf(MemberException.class)
                    .hasMessageContaining(MemberErrors.INVALID_CREDENTIALS.getMessage());
            verify(jwtTokenProvider, never()).parseClaims(anyString());
        }
    }

    @Nested
    @DisplayName("logout() 메서드")
    class LogoutTest {

        @Test
        @DisplayName("정상적인 로그아웃 시 블랙리스트에 토큰을 추가한다")
        void logout_success() {
            // given
            String refreshToken = "validRefreshToken";

            // when
            authService.logout(refreshToken);

            // then
            verify(refreshTokenBlacklistService).addToBlacklist(refreshToken);
        }
    }
}

