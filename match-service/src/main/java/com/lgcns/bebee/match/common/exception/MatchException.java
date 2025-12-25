package com.lgcns.bebee.match.common.exception;

import com.lgcns.bebee.common.exception.DomainException;

/**
 * enum으로 정의한 에러 코드를 예외로 던질 수 있게 감싸주는 역할
 */
public class MatchException extends DomainException {
    private MatchErrors error;

    public MatchException(MatchErrors error) {
        super(error);
        this.error = error;
    }

    public MatchErrors getError() {
        return error;
    }
}
