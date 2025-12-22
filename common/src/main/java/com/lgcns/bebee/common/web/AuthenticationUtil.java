package com.lgcns.bebee.common.web;

import jakarta.servlet.http.HttpServletRequest;

import static com.lgcns.bebee.common.exception.AuthenticationErrors.INVALID_TOKEN_TYPE;
import static com.lgcns.bebee.common.exception.AuthenticationErrors.TOKEN_MISSING;

public class AuthenticationUtil {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    public static final String MEMBER_KEY = "memberId";

    private AuthenticationUtil() {
    }

    public static String resolveToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(AUTHORIZATION_HEADER);
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw TOKEN_MISSING.toException();
        }

        if (jwtToken.startsWith(BEARER_PREFIX)) {
            return jwtToken.substring(BEARER_PREFIX.length());
        }

        throw INVALID_TOKEN_TYPE.toException();
    }
}
