package com.lgcns.bebee.notification.application;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.notification.domain.entity.PushNotificationSubscription;
import com.lgcns.bebee.notification.domain.repository.PushNotificationSubscriptionRepository;
import com.lgcns.bebee.notification.domain.service.PushNotificationSubscriptionManagement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterFcmTokenUseCase implements UseCase<RegisterFcmTokenUseCase.Param, Void> {
    private final PushNotificationSubscriptionManagement subscriptionManagement;
    private final PushNotificationSubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public Void execute(Param params) {
        subscriptionManagement.validateSubscription(params.memberId, params.fcmToken);

        PushNotificationSubscription subscription = PushNotificationSubscription.create(params.memberId, params.fcmToken, params.deviceType);

        subscriptionRepository.save(subscription);

        return null;
    }

    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final String fcmToken;
        private final String deviceType;
    }
}
