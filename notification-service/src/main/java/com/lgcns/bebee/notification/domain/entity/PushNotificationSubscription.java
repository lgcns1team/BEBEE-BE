package com.lgcns.bebee.notification.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushNotificationSubscription extends BaseTimeEntity {
    @Id
    @Tsid
    private Long webPushNotificationId;

    @Column(nullable = false)
    private Long subscriberId;

    @Column(nullable = false)
    private Long fcmTokenId;
}
