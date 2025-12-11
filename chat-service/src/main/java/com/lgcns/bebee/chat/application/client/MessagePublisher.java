package com.lgcns.bebee.chat.application.client;

import com.lgcns.bebee.chat.domain.entity.Chat;

public interface MessagePublisher {
    void publishToMember(Long senderId, Long receiverId, Chat chat);
}
