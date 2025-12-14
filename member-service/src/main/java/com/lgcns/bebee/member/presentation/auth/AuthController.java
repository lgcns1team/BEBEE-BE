package com.lgcns.bebee.member.presentation.auth;

import com.lgcns.bebee.member.common.exception.AuthErrors;
import com.lgcns.bebee.member.domain.service.AuthService;
import com.lgcns.bebee.member.infrastructure.security.JwtProperties;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider.TokenPair;
import com.lgcns.bebee.member.presentation.auth.dto.LoginRequest;
import com.lgcns.bebee.member.presentation.auth.dto.SignupRequest;
import com.lgcns.bebee.member.presentation.auth.dto.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;

    public AuthController(AuthService authService, JwtProperties jwtProperties) {
        this.authService = authService;
        this.jwtProperties = jwtProperties;
    }

    @PostMapping("/signup")
    public ResponseEntity<TokenResponse> signup(@Valid @RequestBody SignupRequest request) {
        TokenPair tokens = authService.signup(mapSignupCommand(request));
        ResponseCookie refreshCookie = buildRefreshCookie(tokens.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(TokenResponse.of(tokens.accessToken(), tokens.accessTokenExpiry()));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenPair tokens = authService.login(new AuthService.LoginCommand(request.getEmail(), request.getPassword()));
        ResponseCookie refreshCookie = buildRefreshCookie(tokens.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(TokenResponse.of(tokens.accessToken(), tokens.accessTokenExpiry()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(HttpServletRequest servletRequest) {
        String refresh = extractRefreshToken(servletRequest);
        TokenPair tokens = authService.reissue(refresh);
        ResponseCookie refreshCookie = buildRefreshCookie(tokens.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(TokenResponse.of(tokens.accessToken(), tokens.accessTokenExpiry()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie expired = ResponseCookie.from(jwtProperties.getRefreshCookieName(), "")
                .httpOnly(true)
                .secure(jwtProperties.isRefreshCookieSecure())
                .path(jwtProperties.getRefreshCookiePath())
                .maxAge(Duration.ZERO)
                .sameSite(jwtProperties.getRefreshCookieSameSite())
                .domain(jwtProperties.getRefreshCookieDomain())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, expired.toString());
        return ResponseEntity.noContent().build();
    }

    private AuthService.SignupCommand mapSignupCommand(SignupRequest request) {
        return new AuthService.SignupCommand(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getNickname(),
                request.getBirthDate(),
                request.getGender(),
                request.getPhoneNumber(),
                request.getRole(),
                request.getAddressRoad(),
                request.getLatitude(),
                request.getLongitude(),
                request.getDistrictCode()
        );
    }

    private ResponseCookie buildRefreshCookie(String refreshToken) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(jwtProperties.getRefreshCookieName(), refreshToken)
                .httpOnly(true)
                .secure(jwtProperties.isRefreshCookieSecure())
                .path(jwtProperties.getRefreshCookiePath())
                .maxAge(Duration.ofSeconds(jwtProperties.getRefreshTokenValiditySeconds()))
                .sameSite(jwtProperties.getRefreshCookieSameSite());
        if (jwtProperties.getRefreshCookieDomain() != null) {
            builder.domain(jwtProperties.getRefreshCookieDomain());
        }
        return builder.build();
    }

    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw AuthErrors.INVALID_CREDENTIALS.toException();
        }
        return java.util.Arrays.stream(request.getCookies())
                .filter(c -> jwtProperties.getRefreshCookieName().equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(AuthErrors.INVALID_CREDENTIALS::toException);
    }
}

