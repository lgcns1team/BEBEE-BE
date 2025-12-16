package com.lgcns.bebee.notification.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@PrimaryKeyJoinColumn(name = "in_app_notification_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("CHAT")
public class ChatInAppNotification extends InAppNotification {
    @Column(nullable = false)
    private Long chatRoomId;

    @Column(length = 255)
    private String messagePreview;

    @Column(nullable = false)
    private Integer messageCount = 1;
}
