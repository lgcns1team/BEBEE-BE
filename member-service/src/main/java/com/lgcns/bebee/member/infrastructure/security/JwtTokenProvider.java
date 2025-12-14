package com.lgcns.bebee.member.infrastructure.security;

import com.lgcns.bebee.member.domain.entity.Member;
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
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public TokenPair generateTokens(Member member) {
        Instant now = Instant.now();
        String accessToken = createToken(member, now, jwtProperties.getAccessTokenValiditySeconds());
        String refreshToken = createRefreshToken(member, now, jwtProperties.getRefreshTokenValiditySeconds());
        return new TokenPair(accessToken, refreshToken, now.plusSeconds(jwtProperties.getAccessTokenValiditySeconds()));
    }

    public TokenPair reissueTokens(Member member, String refreshToken) {
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

    private String createToken(Member member, Instant issuedAt, long validitySeconds) {
        Instant expiry = issuedAt.plusSeconds(validitySeconds);
        return Jwts.builder()
                .setSubject(member.getEmail())
                .claim("memberId", member.getMemberId())
                .claim("role", member.getRole().name())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiry))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(Member member, Instant issuedAt, long validitySeconds) {
        Instant expiry = issuedAt.plusSeconds(validitySeconds);
        return Jwts.builder()
                .setSubject(member.getEmail())
                .claim("role", member.getRole().name())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiry))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public record TokenPair(String accessToken, String refreshToken, Instant accessTokenExpiry) { }
}

