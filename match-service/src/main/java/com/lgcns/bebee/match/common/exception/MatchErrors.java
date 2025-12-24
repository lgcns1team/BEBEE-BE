package com.lgcns.bebee.match.common.exception;

import com.lgcns.bebee.common.exception.DomainException;
import com.lgcns.bebee.common.exception.ErrorInfo;
import org.springframework.http.HttpStatus;

public enum MatchErrors implements ErrorInfo {
    POST_NOT_FOUND("존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),

    MEMBER_NOT_FOUND("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),

    MATCH_NOT_FOUND("매칭 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND("회원 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ONLY_DISABLED_MEMBERS_ALLOWED("장애인 회원만 확인서를 생성할 수 있습니다.", HttpStatus.FORBIDDEN),
    AGREEMENT_NOT_FOUND("매칭 확인서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ALREADY_CONFIRMED_AGREEMENT("이미 수락된 매칭 확인서입니다.", HttpStatus.CONFLICT),
    CANNOT_REFUSE_CONFIRMED_AGREEMENT("이미 수락된 매칭 확인서는 거절할 수 없습니다.", HttpStatus.CONFLICT)
    ;

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
