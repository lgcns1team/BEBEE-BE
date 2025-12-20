package com.lgcns.bebee.notification.infrastructure.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.lgcns.bebee.notification.application.client.PushNotificationClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Firebase Cloud Messaging을 사용한 푸시 알림 클라이언트 구현체
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FcmPushNotificationPublisher implements PushNotificationClient {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendMessage(String token, String title, String body, Map<String, String> data) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putAllData(data)
                    .build();

            String messageId = firebaseMessaging.send(message);
            log.info("FCM message with data sent successfully: messageId={}, token={}", messageId, token);

        } catch (FirebaseMessagingException e) {
            log.error("Failed to send FCM message with data: token={}, error={}", token, e.getMessage(), e);
            throw new RuntimeException("FCM 메시지 전송 실패", e);
        }
    }
}