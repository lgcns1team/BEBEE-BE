package com.lgcns.bebee.match.exception;

import com.lgcns.bebee.common.exception.InvalidParamErrorInfo;

public enum MatchInvalidParamException implements InvalidParamErrorInfo {
    // 파라미터 검증 에러 코드 정의
    // 아래는 예시로 작성. 개발 중 직접 추가 또는 수정 필요
    USER_NOT_FOUND("해당 유저를 찾을 수 없습니다.", "email"),
    EMAIL_REQUIRED("이메일이 설정되지 않았습니다.", "email"),
    EMAIL_ALREADY_EXISTS("이미 존재하는 이메일입니다.", "email");

    private final String desc;
    private final String field;

    MatchInvalidParamException(String desc, String field) {
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
