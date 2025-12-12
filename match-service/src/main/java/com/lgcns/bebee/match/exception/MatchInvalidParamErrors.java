package com.lgcns.bebee.match.exception;

import com.lgcns.bebee.common.exception.InvalidParamErrorInfo;

public enum MatchInvalidParamErrors implements InvalidParamErrorInfo {
    /**
     * 파라미터 검증 에러 코드 정의
     * 아래는 예시로 작성. 개발 중 직접 추가 또는 수정 필요
     */
    REQUIRED_FIELD("필수 입력값입니다.", "field"),
    INVALID_FORMAT("입력 형식이 올바르지 않습니다.", "field"),
    OUT_OF_RANGE("입력값이 유효 범위를 벗어났습니다.", "field");

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
