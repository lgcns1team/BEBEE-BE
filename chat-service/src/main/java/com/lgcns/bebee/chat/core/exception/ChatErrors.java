package com.lgcns.bebee.chat.core.exception;

import com.lgcns.bebee.common.exception.DomainException;
import com.lgcns.bebee.common.exception.ErrorInfo;

public enum ChatErrors implements ErrorInfo {
    CHATROOM_NOT_FOUND("해당 채팅방을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND("해당 회원을 찾을 수 없습니다.");

    private final String desc;

    ChatErrors(String desc){
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    public DomainException toException() {
        return new ChatException(this);
    }
}
