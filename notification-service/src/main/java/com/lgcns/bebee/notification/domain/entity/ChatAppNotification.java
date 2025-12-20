package com.lgcns.bebee.notification.domain.entity;

import com.lgcns.bebee.notification.domain.entity.vo.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Entity
@PrimaryKeyJoinColumn(name = "in_app_notification_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("CHAT")
public class ChatAppNotification extends AppNotification {
    @Column(nullable = false)
    private Long chatroomId;

    @Column(length = 255)
    private String messagePreview;

    @Column(nullable = false)
    private Integer messageCount = 1;

    public static ChatAppNotification create(Long senderId, Long receiverId, Long chatroomId, String messagePreview) {
        ChatAppNotification inAppNotification = new ChatAppNotification();
        inAppNotification.senderId = senderId;
        inAppNotification.receiverId = receiverId;
        inAppNotification.type = NotificationType.CHAT;
        inAppNotification.chatroomId = chatroomId;
        inAppNotification.messagePreview = messagePreview;
        return inAppNotification;
    }

    @Override
    public String getTitle() {
        return "새로운 메시지가 도착했습니다";
    }

    @Override
    public String getBody() {
        return messagePreview != null ? messagePreview : "새로운 메시지를 확인하세요.";
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("type", NotificationType.CHAT.name());
        data.put("senderId", String.valueOf(senderId));
        data.put("receiverId", String.valueOf(receiverId));
        data.put("chatroomId", String.valueOf(chatroomId));
        return data;
    }
}
