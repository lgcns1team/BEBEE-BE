package com.lgcns.bebee.notification.core.exception;

import com.lgcns.bebee.common.exception.InvalidParamErrorInfo;

public enum NotificationInvalidParamErrors implements InvalidParamErrorInfo {
    INVALID_DEVICE_TYPE("유효하지 않은 디바이스 타입입니다.", "deviceType"),
    INVALID_NOTIFICATION_TYPE("유효하지 않은 알림 타입입니다.", "notificationType");

    private final String desc;
    private final String field;

    NotificationInvalidParamErrors(String desc, String field){
        this.desc = desc;
        this.field = field;
    }

    @Override
    public String getDesc() {
        return "";
    }

    @Override
    public String getField() {
        return "";
    }
}
