package com.lgcns.bebee.notification.domain.entity;

import com.lgcns.bebee.notification.domain.entity.vo.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@PrimaryKeyJoinColumn(name = "in_app_notification_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("CHAT")
public class ChatInAppNotification extends InAppNotification{
    @Column(nullable = false)
    private Long chat_room_id;

    @Column(length = 255)
    private String message_preview;

    @Column(nullable = false)
    private Integer message_count = 1;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;
}
