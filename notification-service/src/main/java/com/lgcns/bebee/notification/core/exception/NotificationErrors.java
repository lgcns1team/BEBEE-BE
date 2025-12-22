package com.lgcns.bebee.notification.core.exception;

import com.lgcns.bebee.common.exception.DomainException;
import com.lgcns.bebee.common.exception.ErrorInfo;

public enum NotificationErrors implements ErrorInfo {
    TOKEN_ALREADY_EXISTS("이미 등록된 FCM 토큰입니다."),
    SUBSCRIPTION_NOT_FOUND("해당 회원에 대한 구독 정보를 찾을 수 없습니다.");

    private final String desc;

    NotificationErrors(String desc){
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public DomainException toException() {
        return new NotificationException(this);
    }
}
