package com.lgcns.bebee.member.core.exception;

import com.lgcns.bebee.common.exception.InvalidParamErrorInfo;

public enum MemberInvalidParamErrors implements InvalidParamErrorInfo {
    EMAIL_NOT_NULL("이메일은 필수 입력 항목입니다.", "email"),
    INVALID_EMAIL_FORMAT("유효하지 않은 이메일 형식입니다.", "email"),
    PASSWORD_NOT_NULL("비밀번호는 필수 입력 항목입니다.", "password"),
    NICKNAME_NOT_NULL("닉네임은 필수 입력 항목입니다.", "nickname"),
    INVALID_ROLE("유효하지 않은 역할입니다.", "role"),
    INVALID_GENDER("유효하지 않은 성별입니다.", "gender");

    private final String desc;
    private final String field;

    MemberInvalidParamErrors(String desc, String field) {
        this.desc = desc;
        this.field = field;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getField() {
        return field;
    }
}

