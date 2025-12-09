package com.lgcns.bebee.notification.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.notification.domain.entity.vo.NotificationType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public class InAppNotification extends BaseTimeEntity {
    @Id
    @Tsid
    private Long inAppNotificationId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Boolean isRead;

    @Column
    private LocalDateTime readAt;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(nullable = false, length = 255)
    private String redirectionUrl;
}
