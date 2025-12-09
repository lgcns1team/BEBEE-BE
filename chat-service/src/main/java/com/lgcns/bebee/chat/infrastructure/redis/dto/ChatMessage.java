package com.lgcns.bebee.chat.infrastructure.redis.dto;

import com.lgcns.bebee.chat.domain.entity.Chat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatMessage implements Serializable {
    private Long id;
    private Long chatroomId;
    private Long senderId;
    private String textContent;
    private Chat.ChatType type;
    private List<String> attachments;
    private Chat.MatchConfirmationContent matchConfirmationContent;
    private LocalDateTime createdAt;

    public static ChatMessage from(Chat chat) {
        return ChatMessage.builder()
                .id(chat.getId())
                .chatroomId(chat.getChatroomId())
                .senderId(chat.getSenderId())
                .textContent(chat.getTextContent())
                .type(chat.getType())
                .attachments(chat.getAttachments())
                .matchConfirmationContent(chat.getMatchConfirmationContent())
                .createdAt(chat.getCreatedAt())
                .build();
    }
}