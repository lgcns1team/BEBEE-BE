package com.lgcns.bebee.member.common.exception;

import com.lgcns.bebee.common.exception.InvalidParamErrorInfo;

public enum AuthInvalidParamErrors implements InvalidParamErrorInfo {
    EMAIL_ALREADY_EXISTS("이미 존재하는 이메일입니다.", "email"),
    NICKNAME_ALREADY_EXISTS("이미 존재하는 닉네임입니다.", "nickname"),
    INVALID_EMAIL_FORMAT("이메일 형식이 올바르지 않습니다.", "email"),
    INVALID_PASSWORD_FORMAT("비밀번호 정책을 만족하지 않습니다.", "password"),
    INVALID_NICKNAME_FORMAT("닉네임 형식이 올바르지 않습니다.", "nickname"),
    INVALID_ROLE("유효하지 않은 역할입니다.", "role"),
    INVALID_GENDER("유효하지 않은 성별입니다.", "gender");

    private final String desc;
    private final String field;

    AuthInvalidParamErrors(String desc, String field) {
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

