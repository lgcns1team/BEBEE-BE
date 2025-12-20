package com.lgcns.bebee.member.common.exception;

import com.lgcns.bebee.common.exception.DomainException;
import com.lgcns.bebee.common.exception.ErrorInfo;

public enum AuthErrors implements ErrorInfo {
    INVALID_CREDENTIALS("이메일 또는 비밀번호가 올바르지 않습니다."),
    MEMBER_STATUS_PENDING("승인 대기 중인 계정입니다."),
    MEMBER_STATUS_REJECTED("승인 거절된 계정입니다."),
    MEMBER_STATUS_WITHDRAWN("탈퇴 처리된 계정입니다.");

    private final String desc;

    AuthErrors(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public DomainException toException() {
        return new AuthException(this);
    }
}

