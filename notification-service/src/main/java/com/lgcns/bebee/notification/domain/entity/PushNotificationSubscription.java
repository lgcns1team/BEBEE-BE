package com.lgcns.bebee.notification.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.notification.domain.entity.vo.DeviceType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushNotificationSubscription extends BaseTimeEntity {
    @Id
    @Tsid @Column(name = "push_notification_subscription_id")
    private Long id;

    @Column(nullable = false)
    private Long subscriberId;

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    public static PushNotificationSubscription create(
            Long subscriberId,
            String token,
            String deviceType
    ){
        PushNotificationSubscription subscription = new PushNotificationSubscription();
        subscription.subscriberId = subscriberId;
        subscription.token = token;
        subscription.deviceType = DeviceType.from(deviceType);

        return subscription;
    }
}
