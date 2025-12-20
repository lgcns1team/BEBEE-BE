package com.lgcns.bebee.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private long accessTokenValiditySeconds = 3600;
    private long refreshTokenValiditySeconds = 1209600;
    private String refreshCookieName = "refreshToken";
    private String refreshCookiePath = "/";
    private String refreshCookieDomain;
    private boolean refreshCookieSecure = true;
    private String refreshCookieSameSite = "Lax";
}

