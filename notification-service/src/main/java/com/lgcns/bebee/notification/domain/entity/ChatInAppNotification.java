package com.lgcns.bebee.notification.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
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
public class ChatInAppNotification extends InAppNotification {
    @Column(nullable = false)
    private Long chatRoomId;

    @Column(length = 255)
    private String messagePreview;

    @Column(nullable = false)
    private Integer messageCount = 1;
}
