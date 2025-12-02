package com.lgcns.bebee.common.exception;

public interface ErrorInfo {
    String getDesc();
    DomainException toException();

    default String getMessage(){
        return "["+ getCode() + "] " + getDesc();
    }

    default String getCode(){
        return ((Enum<?>) this).name();
    }
}
