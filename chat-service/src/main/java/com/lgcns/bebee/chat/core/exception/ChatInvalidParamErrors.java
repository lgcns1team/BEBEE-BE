package com.lgcns.bebee.chat.core.exception;

import com.lgcns.bebee.common.exception.InvalidParamErrorInfo;

public enum ChatInvalidParamErrors implements InvalidParamErrorInfo {
    INVALID_CHAT_TYPE("유효하지 않은 채팅 타입입니다.", "chatType"),
    INVALID_MATCH_STATUS("유효하지 않은 매칭 유협입니다.", "matchStatus");

    private final String desc;
    private final String field;

    ChatInvalidParamErrors(String desc, String field) {
        this.desc = desc;
        this.field = field;
    }

    @Override
    public String getDesc() {
        return desc;
    }


    @Override
    public String getField() {
        return field;
    }
}
