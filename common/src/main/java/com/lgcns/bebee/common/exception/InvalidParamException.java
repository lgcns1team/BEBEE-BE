package com.lgcns.bebee.common.exception;

public class InvalidParamException extends RuntimeException {
    public InvalidParamErrorInfo error;
    private String field;

    public InvalidParamException(InvalidParamErrorInfo error) {
        this.error = error;
        this.field = error.getField();
    }

    public InvalidParamException(InvalidParamErrorInfo error, String field) {
        this.error = error;
        this.field = field;  // 실제 필드명으로 오버라이드
    }

    public String getField() {
        return field;
    }

    @Override
    public String getMessage() {
        return String.format("%s: %s", field, error.getDesc());
    }

}
