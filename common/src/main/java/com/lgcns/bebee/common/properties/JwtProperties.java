package com.lgcns.bebee.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String issuer,
        String secret,
        Long accessTokenExpiresTimeIn,
        Long refreshTokenExpiresTimeIn
) {
//    private String refreshCookieName = "refreshToken";
//    private String refreshCookiePath = "/";
//    private String refreshCookieDomain;
//    private boolean refreshCookieSecure = true;
//    private String refreshCookieSameSite = "Lax";

    public JwtProperties{
        if(issuer == null){
            issuer = "bebee";
        }
    }
}

