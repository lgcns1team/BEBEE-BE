package com.lgcns.bebee.member.infrastructure.security;

import com.lgcns.bebee.common.properties.JwtProperties;
import com.lgcns.bebee.member.application.client.TokenProvider;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.vo.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenProvider {
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public TokenInfo generateTokens(Member member) {
        Instant now = Instant.now();
        String accessToken = createToken(member, now, jwtProperties.accessTokenExpiresTimeIn());
        String refreshToken = createToken(member, now, jwtProperties.refreshTokenExpiresTimeIn());
        return new TokenInfo(accessToken, refreshToken, jwtProperties.refreshTokenExpiresTimeIn());
    }

    public TokenInfo reissueTokens(Member member, String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        if (!claims.getSubject().equals(member.getEmail())) {
            throw new IllegalArgumentException("Invalid refresh token owner");
        }
        return generateTokens(member);
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String createToken(Member member, Instant issuedAt, long expiresTime) {
        Instant expiry = issuedAt.plusSeconds(expiresTime);
        return Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .claim("role", member.getRole().name())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiry))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}

