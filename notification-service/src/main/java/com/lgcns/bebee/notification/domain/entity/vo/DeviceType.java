package com.lgcns.bebee.notification.domain.entity.vo;

import com.lgcns.bebee.notification.core.exception.NotificationInvalidParamErrors;

public enum DeviceType {
    WEB_PC,
    WEB_IOS,
    WEB_ANDROID;

    public static DeviceType from(String type){
        try{
            return DeviceType.valueOf(type.toUpperCase());
        }catch(IllegalArgumentException e){
            throw NotificationInvalidParamErrors.INVALID_DEVICE_TYPE.toException();
        }
    }
}
