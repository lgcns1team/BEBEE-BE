package com.lgcns.bebee.match.common.exception;

import com.lgcns.bebee.common.exception.DomainException;
import com.lgcns.bebee.common.exception.ErrorInfo;
import org.springframework.http.HttpStatus;

public enum MatchErrors implements ErrorInfo {
    /**
     * 에러 코드 정의
     * 아래는 예시로 작성. 개발 중 직접 추가 또는 수정 필요
     */
    MATCH_NOT_FOUND("매칭 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AGREEMENT_NOT_FOUND("매칭 확인서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN("접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String desc;
    private final HttpStatus httpStatus;

    MatchErrors(String desc, HttpStatus httpStatus) {
        this.desc = desc;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public DomainException toException() {
        return new MatchException(this);
    }
}
