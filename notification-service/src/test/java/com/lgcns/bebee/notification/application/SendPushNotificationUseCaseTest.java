package com.lgcns.bebee.notification.application;

import com.lgcns.bebee.notification.application.client.PushNotificationClient;
import com.lgcns.bebee.notification.domain.entity.*;
import com.lgcns.bebee.notification.domain.repository.AppNotificationRepository;
import com.lgcns.bebee.notification.domain.service.PushNotificationSubscriptionManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("SendPushNotificationUseCase 단위 테스트")
class SendPushNotificationUseCaseTest {

    @Mock
    private PushNotificationSubscriptionManagement subscriptionManagement;

    @Mock
    private PushNotificationClient pushNotificationClient;

    @Mock
    private AppNotificationRepository appNotificationRepository;

    @InjectMocks
    private SendPushNotificationUseCase sendPushNotificationUseCase;

    private static final Long SENDER_ID = 1L;
    private static final Long RECEIVER_ID = 2L;
    private static final String FCM_TOKEN = "test-fcm-token-xyz123";

    private PushNotificationSubscription subscription;

    @BeforeEach
    void setUp() {
        subscription = PushNotificationSubscription.create(
                RECEIVER_ID,
                FCM_TOKEN,
                "WEB_PC"
        );
    }

    @Test
    @DisplayName("APPLICATION 타입 푸시 알림을 성공적으로 전송한다")
    void success_SendApplicationNotification() {
        // given
        Long applicationId = 100L;
        SendPushNotificationUseCase.Param param = new SendPushNotificationUseCase.Param(
                SENDER_ID,
                RECEIVER_ID,
                "APPLICATION",
                applicationId,
                null,
                null,
                null
        );

        given(subscriptionManagement.getExistingPushNotificationSubscription(RECEIVER_ID))
                .willReturn(subscription);

        // when
        sendPushNotificationUseCase.execute(param);

        // then
        then(subscriptionManagement).should(times(1))
                .getExistingPushNotificationSubscription(RECEIVER_ID);
        then(pushNotificationClient).should(times(1))
                .sendMessage(eq(FCM_TOKEN), anyString(), anyString(), anyMap());
        then(appNotificationRepository).should(times(1))
                .save(any(ApplicationAppNotification.class));
    }

    @Test
    @DisplayName("MATCH 타입 푸시 알림을 성공적으로 전송한다")
    void success_SendMatchNotification() {
        // given
        Long matchId = 200L;
        SendPushNotificationUseCase.Param param = new SendPushNotificationUseCase.Param(
                SENDER_ID,
                RECEIVER_ID,
                "MATCH",
                null,
                matchId,
                null,
                null
        );

        given(subscriptionManagement.getExistingPushNotificationSubscription(RECEIVER_ID))
                .willReturn(subscription);

        // when
        sendPushNotificationUseCase.execute(param);

        // then
        then(subscriptionManagement).should(times(1))
                .getExistingPushNotificationSubscription(RECEIVER_ID);
        then(pushNotificationClient).should(times(1))
                .sendMessage(eq(FCM_TOKEN), anyString(), anyString(), anyMap());
        then(appNotificationRepository).should(times(1))
                .save(any(MatchAppNotification.class));
    }

    @Test
    @DisplayName("CHAT 타입 푸시 알림을 성공적으로 전송한다")
    void success_SendChatNotification() {
        // given
        Long chatroomId = 300L;
        String messagePreview = "안녕하세요!";
        SendPushNotificationUseCase.Param param = new SendPushNotificationUseCase.Param(
                SENDER_ID,
                RECEIVER_ID,
                "CHAT",
                null,
                null,
                chatroomId,
                messagePreview
        );

        given(subscriptionManagement.getExistingPushNotificationSubscription(RECEIVER_ID))
                .willReturn(subscription);

        // when
        sendPushNotificationUseCase.execute(param);

        // then
        then(subscriptionManagement).should(times(1))
                .getExistingPushNotificationSubscription(RECEIVER_ID);
        then(pushNotificationClient).should(times(1))
                .sendMessage(eq(FCM_TOKEN), anyString(), anyString(), anyMap());
        then(appNotificationRepository).should(times(1))
                .save(any(ChatAppNotification.class));
    }
}