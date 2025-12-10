package com.lgcns.bebee.match.exception;

import com.lgcns.bebee.common.exception.DomainException;
import com.lgcns.bebee.common.exception.ErrorInfo;
import org.springframework.http.HttpStatus;

public enum MatchErrors implements ErrorInfo {
    // 에러 코드 정의
    // 아래는 예시로 작성. 개발 중 직접 추가 또는 수정 필요
    NOT_FOUND("해당 유저를 찾을 수 없습니다", HttpStatus.NOT_FOUND);

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
