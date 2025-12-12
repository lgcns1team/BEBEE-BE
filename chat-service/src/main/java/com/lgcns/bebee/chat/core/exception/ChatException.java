package com.lgcns.bebee.chat.core.exception;

import com.lgcns.bebee.common.exception.DomainException;

public class ChatException extends DomainException {
    private ChatErrors error;

    public ChatException(ChatErrors error) {
        super(error);
        this.error = error;
    }
}
