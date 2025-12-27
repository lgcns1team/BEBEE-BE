package com.lgcns.bebee.member.presentation.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.bebee.member.domain.repository.DocumentRepository;
import com.lgcns.bebee.member.domain.repository.DocumentVerificationRepository;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import com.lgcns.bebee.member.application.usecase.LoginUseCase;
import com.lgcns.bebee.member.application.usecase.LogoutUseCase;
import com.lgcns.bebee.member.application.usecase.ReissueUseCase;
import com.lgcns.bebee.member.application.usecase.SignUpUseCase;
import com.lgcns.bebee.member.infrastructure.security.CurrentMemberArgumentResolver;
import com.lgcns.bebee.common.config.JwtProperties;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider.TokenPair;
import com.lgcns.bebee.common.exception.GlobalExceptionHandler;
import jakarta.persistence.EntityManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import com.lgcns.bebee.member.presentation.dto.MemberLoginReqDTO;
import com.lgcns.bebee.member.presentation.dto.MemberSignUpReqDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AuthController Integration Test
 *
 * 테스트 범위: API 엔드포인트, HTTP 요청/응답, 쿠키 처리
 * Mock 대상: SignupUseCase, LoginUseCase, ReissueUseCase, LogoutUseCase, JwtProperties
 */
@WebMvcTest(controllers = AuthController.class)
@ActiveProfiles("test")
@DisplayName("AuthController API 테스트")
@Import({GlobalExceptionHandler.class})
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignUpUseCase signUpUseCase;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private ReissueUseCase reissueUseCase;

    @MockBean
    private LogoutUseCase logoutUseCase;

    @MockBean
    private JwtProperties jwtProperties;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CurrentMemberArgumentResolver currentMemberArgumentResolver;

    /**
     * 테스트용 검증 예외 핸들러: MethodArgumentNotValidException을 400으로 반환
     */
    @RestControllerAdvice
    static class ValidationHandler {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Void> handleValidation(MethodArgumentNotValidException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @MockBean
    private EntityManager entityManager;

    @Nested
    @DisplayName("POST /auth/signup")
    class SignupTest {

        @Test
        @DisplayName("정상적인 회원가입 요청 시 200 OK와 액세스 토큰을 반환한다")
        void signup_success() throws Exception {
            // given
                    MemberSignUpReqDTO request = new MemberSignUpReqDTO();
            ReflectionTestUtils.setField(request, "email", "test@example.com");
            ReflectionTestUtils.setField(request, "password", "Test1234!@");
            ReflectionTestUtils.setField(request, "name", "홍길동");
            ReflectionTestUtils.setField(request, "nickname", "길동이");
            ReflectionTestUtils.setField(request, "birthDate", LocalDate.of(1990, 1, 1));
            ReflectionTestUtils.setField(request, "gender", "MALE");
            ReflectionTestUtils.setField(request, "phoneNumber", "010-1234-5678");
            ReflectionTestUtils.setField(request, "role", "DISABLED");
            ReflectionTestUtils.setField(request, "addressRoad", "서울시 강남구");
            ReflectionTestUtils.setField(request, "latitude", 37.5665);
            ReflectionTestUtils.setField(request, "longitude", 126.9780);
            ReflectionTestUtils.setField(request, "districtCode", "1168010100");

            JwtTokenProvider.TokenPair tokenPair = new JwtTokenProvider.TokenPair(
                    "accessToken",
                    "refreshToken",
                    Instant.now().plusSeconds(3600)
            );

                    given(signUpUseCase.execute(any())).willReturn(tokenPair);
            given(jwtProperties.getRefreshCookieName()).willReturn("refreshToken");
            given(jwtProperties.getRefreshTokenValiditySeconds()).willReturn(1209600L);
            given(jwtProperties.isRefreshCookieSecure()).willReturn(false);
            given(jwtProperties.getRefreshCookiePath()).willReturn("/");
            given(jwtProperties.getRefreshCookieSameSite()).willReturn("Lax");

            // when & then
            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("accessToken"))
                    .andExpect(jsonPath("$.accessTokenExpiresAt").exists())
                    .andExpect(cookie().exists("refreshToken"))
                    .andExpect(cookie().httpOnly("refreshToken", true));
        }

        @Test
        @DisplayName("유효하지 않은 요청 데이터 시 400 Bad Request를 반환한다")
        void signup_invalidRequest() throws Exception {
            // given
                    MemberSignUpReqDTO request = new MemberSignUpReqDTO();
            // 필수 필드 누락

            // when & then
            mockMvc.perform(post("/auth/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /auth/login")
    class LoginTest {

        @Test
        @DisplayName("정상적인 로그인 요청 시 200 OK와 액세스 토큰을 반환한다")
        void login_success() throws Exception {
            // given
                    MemberLoginReqDTO request = new MemberLoginReqDTO();
            ReflectionTestUtils.setField(request, "email", "test@example.com");
            ReflectionTestUtils.setField(request, "password", "Test1234!@");

            JwtTokenProvider.TokenPair tokenPair = new JwtTokenProvider.TokenPair(
                    "accessToken",
                    "refreshToken",
                    Instant.now().plusSeconds(3600)
            );

                    given(loginUseCase.execute(any())).willReturn(tokenPair);
            given(jwtProperties.getRefreshCookieName()).willReturn("refreshToken");
            given(jwtProperties.getRefreshTokenValiditySeconds()).willReturn(1209600L);
            given(jwtProperties.isRefreshCookieSecure()).willReturn(false);
            given(jwtProperties.getRefreshCookiePath()).willReturn("/");
            given(jwtProperties.getRefreshCookieSameSite()).willReturn("Lax");

            // when & then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("accessToken"))
                    .andExpect(jsonPath("$.accessTokenExpiresAt").exists())
                    .andExpect(cookie().exists("refreshToken"))
                    .andExpect(cookie().httpOnly("refreshToken", true));
        }

        @Test
        @DisplayName("유효하지 않은 요청 데이터 시 400 Bad Request를 반환한다")
        void login_invalidRequest() throws Exception {
            // given
                    MemberLoginReqDTO request = new MemberLoginReqDTO();
            // 필수 필드 누락

            // when & then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /auth/reissue")
    class ReissueTest {

        @Test
        @DisplayName("정상적인 토큰 재발급 요청 시 200 OK와 새 토큰을 반환한다")
        void reissue_success() throws Exception {
            // given
            JwtTokenProvider.TokenPair tokenPair = new JwtTokenProvider.TokenPair(
                    "newAccessToken",
                    "newRefreshToken",
                    Instant.now().plusSeconds(3600)
            );

                    given(reissueUseCase.execute(any())).willReturn(tokenPair);
            given(jwtProperties.getRefreshCookieName()).willReturn("refreshToken");
            given(jwtProperties.getRefreshTokenValiditySeconds()).willReturn(1209600L);
            given(jwtProperties.isRefreshCookieSecure()).willReturn(false);
            given(jwtProperties.getRefreshCookiePath()).willReturn("/");
            given(jwtProperties.getRefreshCookieSameSite()).willReturn("Lax");

            // when & then
            mockMvc.perform(post("/api/auth/reissue")
                            .cookie(new jakarta.servlet.http.Cookie("refreshToken", "oldRefreshToken")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("newAccessToken"))
                    .andExpect(jsonPath("$.accessTokenExpiresAt").exists())
                    .andExpect(cookie().exists("refreshToken"))
                    .andExpect(cookie().httpOnly("refreshToken", true));
        }

        @Test
        @DisplayName("리프레시 토큰 쿠키가 없을 시 401 Unauthorized를 반환한다")
        void reissue_noCookie() throws Exception {
            // when & then
            mockMvc.perform(post("/auth/reissue"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("POST /auth/logout")
    class LogoutTest {

        @Test
        @DisplayName("정상적인 로그아웃 요청 시 204 No Content를 반환하고 쿠키를 만료시킨다")
        void logout_success() throws Exception {
            // given
            given(jwtProperties.getRefreshCookieName()).willReturn("refreshToken");
            given(jwtProperties.isRefreshCookieSecure()).willReturn(false);
            given(jwtProperties.getRefreshCookiePath()).willReturn("/");
            given(jwtProperties.getRefreshCookieSameSite()).willReturn("Lax");

            // when & then
            mockMvc.perform(post("/api/auth/logout")
                            .cookie(new jakarta.servlet.http.Cookie("refreshToken", "refreshToken")))
                    .andExpect(status().isNoContent())
                    .andExpect(cookie().maxAge("refreshToken", 0));
        }

        @Test
        @DisplayName("쿠키가 없어도 로그아웃은 성공한다")
        void logout_noCookie() throws Exception {
            // given
            given(jwtProperties.getRefreshCookieName()).willReturn("refreshToken");
            given(jwtProperties.isRefreshCookieSecure()).willReturn(false);
            given(jwtProperties.getRefreshCookiePath()).willReturn("/");
            given(jwtProperties.getRefreshCookieSameSite()).willReturn("Lax");

            // when & then
            mockMvc.perform(post("/auth/logout"))
                    .andExpect(status().isNoContent());
        }
    }
}

