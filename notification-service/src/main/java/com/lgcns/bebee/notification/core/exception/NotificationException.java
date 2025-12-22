package com.lgcns.bebee.notification.core.exception;

import com.lgcns.bebee.common.exception.DomainException;

public class NotificationException extends DomainException {
    private final NotificationErrors error;

    public NotificationException(NotificationErrors error) {
        super(error);
        this.error = error;
    }
}
