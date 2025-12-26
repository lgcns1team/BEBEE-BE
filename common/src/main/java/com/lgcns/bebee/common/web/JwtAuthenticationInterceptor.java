package com.lgcns.bebee.common.web;

import com.lgcns.bebee.common.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.lgcns.bebee.common.exception.AuthenticationErrors.*;
import static com.lgcns.bebee.common.web.AuthenticationUtil.*;

/**
 * JWT 인증 인터셉터
 * - Authorization 헤더에서 Bearer 토큰을 추출
 * - 토큰을 파싱하여 memberId를 추출
 * - 요청 속성에 memberId를 저장하여 컨트롤러에서 사용 가능하도록 함
 */

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;

    private SecretKey secretKey;

    @PostConstruct
    void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            String token = resolveToken(request);

            Claims claims = parseClaims(token);
            String memberId = claims.getSubject();
            request.setAttribute(MEMBER_KEY, memberId);
        }
        return true;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .requireIssuer(jwtProperties.issuer())
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰입니다.");
            throw TOKEN_EXPIRED.toException();
        } catch (JwtException e) {
            log.warn("유효하지 않은 JWT 토큰입니다. {}", e.getMessage());
            throw INVALID_TOKEN.toException();
        }
    }
}
