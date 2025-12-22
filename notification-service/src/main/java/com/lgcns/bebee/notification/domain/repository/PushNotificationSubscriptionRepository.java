package com.lgcns.bebee.notification.domain.repository;

import com.lgcns.bebee.notification.domain.entity.PushNotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PushNotificationSubscriptionRepository extends JpaRepository<PushNotificationSubscription, Long> {
    Optional<PushNotificationSubscription> findBySubscriberId(Long subscriberId);
    Optional<PushNotificationSubscription> findByToken(String token);
}
