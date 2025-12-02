package com.lgcns.bebee.common.exception;

public class InvalidParamException extends RuntimeException {
    public InvalidParamErrorInfo error;

    public InvalidParamException(InvalidParamErrorInfo error) {
        this.error = error;
    }
}
