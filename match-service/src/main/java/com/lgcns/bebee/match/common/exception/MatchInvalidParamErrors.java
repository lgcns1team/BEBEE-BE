package com.lgcns.bebee.match.common.exception;

import com.lgcns.bebee.common.exception.InvalidParamErrorInfo;

public enum MatchInvalidParamErrors implements InvalidParamErrorInfo {
    REQUIRED_FIELD("필수 입력값입니다.", "field"),
    INVALID_FORMAT("입력 형식이 올바르지 않습니다.", "field"),
    OUT_OF_RANGE("입력값이 유효 범위를 벗어났습니다.", "field"),

    INVALID_ENGAGEMENT_TYPE("유효하지 않은 도움 타입 입니다.", "engagement"),
    INVALID_GENDER("유효하지 않은 성별입니다.", "gender")
    ;

    private final String desc;
    private final String field;

    MatchInvalidParamErrors(String desc, String field) {
        this.desc = desc;
        this.field = field;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
