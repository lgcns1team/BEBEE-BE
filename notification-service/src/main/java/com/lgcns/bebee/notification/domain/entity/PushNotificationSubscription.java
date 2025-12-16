package com.lgcns.bebee.notification.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
