package com.lgcns.bebee.common.exception;

public class DomainException extends RuntimeException {
    public DomainException(ErrorInfo error){
        super(error.getMessage());
    }
}
