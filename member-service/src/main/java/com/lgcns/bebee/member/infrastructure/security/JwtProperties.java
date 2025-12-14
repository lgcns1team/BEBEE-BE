package com.lgcns.bebee.member.infrastructure.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private long accessTokenValiditySeconds;
    private long refreshTokenValiditySeconds;
    private String refreshCookieName;
    private String refreshCookiePath = "/";
    private String refreshCookieDomain;
    private boolean refreshCookieSecure = true;
    private String refreshCookieSameSite = "Lax";
}

