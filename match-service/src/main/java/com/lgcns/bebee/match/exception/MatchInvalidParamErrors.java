package com.lgcns.bebee.match.exception;

import com.lgcns.bebee.common.exception.InvalidParamErrorInfo;

public enum MatchInvalidParamErrors implements InvalidParamErrorInfo {
    /**
     * 파라미터 검증 에러 코드 정의
     * 아래는 예시로 작성. 개발 중 직접 추가 또는 수정 필요
     */
    MATCH_NOT_FOUND("매칭 정보를 찾을 수 없습니다.", "match");

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
