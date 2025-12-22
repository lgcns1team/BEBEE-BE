package com.lgcns.bebee.common.exception;

public enum AuthenticationErrors implements ErrorInfo {
    TOKEN_MISSING("토큰이 존재하지 않습니다."),
    INVALID_TOKEN("유효한 토큰이 아닙니다."),
    INVALID_TOKEN_TYPE("Bearer 토큰이 아닙니다."),
    TOKEN_EXPIRED("만료된 토큰입니다."),

    REFRESH_TOKEN_EXPIRED("리프레시 토큰이 만료되었습니다. 다시 로그인해주세요."),
    REFRESH_TOKEN_THEFT_DETECTED("비정상적인 리프레시 토큰 사용이 감지되 로그인이 해제됩니다.")
    ;

    private final String desc;

    AuthenticationErrors(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public DomainException toException() {
        return new AuthenticationException(this);
    }
}
