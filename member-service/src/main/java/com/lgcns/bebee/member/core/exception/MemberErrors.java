package com.lgcns.bebee.member.core.exception;

import com.lgcns.bebee.common.exception.DomainException;
import com.lgcns.bebee.common.exception.ErrorInfo;

public enum MemberErrors implements ErrorInfo {
    MEMBER_NOT_FOUND("해당 id를 가진 회원이 없습니다."),
    MEMBER_NOT_FOUND_BY_EMAIL("해당 이메일을 가진 회원이 없습니다."),
    INVALID_PASSWORD("비밀번호가 올바르지 않습니다."),
    MEMBER_STATUS_PENDING("승인 대기 중인 계정입니다."),
    MEMBER_STATUS_REJECTED("승인 거절된 계정입니다."),
    MEMBER_STATUS_WITHDRAWN("탈퇴 처리된 계정입니다.");

    private final String desc;

    MemberErrors(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public DomainException toException() {
        return new MemberException(this);
    }
}

