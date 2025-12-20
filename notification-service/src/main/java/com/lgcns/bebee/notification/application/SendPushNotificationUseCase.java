package com.lgcns.bebee.notification.application;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.notification.application.client.PushNotificationClient;
import com.lgcns.bebee.notification.domain.entity.*;
import com.lgcns.bebee.notification.domain.entity.vo.NotificationType;
import com.lgcns.bebee.notification.domain.repository.AppNotificationRepository;
import com.lgcns.bebee.notification.domain.service.PushNotificationSubscriptionManagement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendPushNotificationUseCase implements UseCase<SendPushNotificationUseCase.Param, Void> {
    private final PushNotificationSubscriptionManagement subscriptionManagement;
    private final PushNotificationClient pushNotificationClient;

    private final AppNotificationRepository inAppNotificationRepository;

    @Override
    @Transactional
    public Void execute(Param params) {
        // 수신자의 FCM 토큰 조회
        PushNotificationSubscription subscription =
            subscriptionManagement.getExistingPushNotificationSubscription(params.receiverId);

        // 알림 타입에 따른 InAppNotification 생성
        AppNotification appNotification = create(params);

        // 푸시 알림 메시지 구성
        String title = appNotification.getTitle();
        String body = appNotification.getBody();
        Map<String, String> data = appNotification.getData();

        // FCM 푸시 알림 전송
        pushNotificationClient.sendMessage(subscription.getToken(), title, body, data);

        inAppNotificationRepository.save(appNotification);
        return null;
    }

    @RequiredArgsConstructor
    public static class Param implements Params{
        private final Long senderId;
        private final Long receiverId;
        private final String notificationType;
        private final Long applicationId;
        private final Long matchId;
        private final Long chatroomId;
        private final String messagePreview;
    }

    private AppNotification create(Param params){
        NotificationType type = NotificationType.from(params.notificationType);

        return switch(type){
            case APPLICATION -> ApplicationAppNotification.create(params.senderId, params.receiverId, params.applicationId);
            case MATCH -> MatchAppNotification.create(params.senderId, params.receiverId, params.matchId);
            case CHAT -> ChatAppNotification.create(params.senderId, params.receiverId, params.chatroomId, params.messagePreview);
        };
    }
}
