package com.lgcns.bebee.member.common.exception;

import com.lgcns.bebee.common.exception.DomainException;

public class AuthException extends DomainException {

    private final AuthErrors error;

    public AuthException(AuthErrors error) {
        super(error);
        this.error = error;
    }

    public AuthErrors getError() {
        return error;
    }
}

