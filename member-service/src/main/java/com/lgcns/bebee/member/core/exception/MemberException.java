package com.lgcns.bebee.member.core.exception;

import com.lgcns.bebee.common.exception.DomainException;

public class MemberException extends DomainException {

    private final MemberErrors error;

    public MemberException(MemberErrors error) {
        super(error);
        this.error = error;
    }

    public MemberErrors getError() {
        return error;
    }
}

