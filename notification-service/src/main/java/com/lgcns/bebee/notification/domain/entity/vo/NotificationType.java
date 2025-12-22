package com.lgcns.bebee.notification.domain.entity.vo;

import com.lgcns.bebee.notification.core.exception.NotificationInvalidParamErrors;

public enum NotificationType {
    CHAT,
    APPLICATION,
    MATCH;

    public static NotificationType from(String type){
        try{
            return valueOf(type.toUpperCase());
        }catch (IllegalArgumentException e){
            throw NotificationInvalidParamErrors.INVALID_NOTIFICATION_TYPE.toException();
        }
    }
}
