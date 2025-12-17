package com.lgcns.bebee.notification.application;

import com.lgcns.bebee.notification.domain.entity.PushNotificationSubscription;
import com.lgcns.bebee.notification.domain.repository.PushNotificationSubscriptionRepository;
import com.lgcns.bebee.notification.domain.service.PushNotificationSubscriptionManagement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FCM 토큰 등록 유스케이스 테스트")
class RegisterFcmTokenUseCaseTest {

    @Mock
    private PushNotificationSubscriptionManagement subscriptionManagement;

    @Mock
    private PushNotificationSubscriptionRepository subscriptionRepository;

    @InjectMocks
    private RegisterFcmTokenUseCase useCase;

    @Test
    @DisplayName("정상적으로 FCM 토큰이 등록된다")
    void success_RegisterNewToken() {
        // given
        Long memberId = 100L;
        String fcmToken = "test-fcm-token-1234567890";
        String deviceType = "WEB_PC";

        RegisterFcmTokenUseCase.Param param = new RegisterFcmTokenUseCase.Param(
                memberId,
                fcmToken,
                deviceType
        );

        // validateSubscription은 void이므로 아무것도 하지 않음
        doNothing().when(subscriptionManagement).validateSubscription(memberId, fcmToken);

        // when
        useCase.execute(param);

        // then
        verify(subscriptionManagement, times(1)).validateSubscription(memberId, fcmToken);
        verify(subscriptionRepository, times(1)).save(any(PushNotificationSubscription.class));
    }

    @Test
    @DisplayName("여러 디바이스 타입으로 토큰을 등록할 수 있다")
    void success_DifferentDeviceTypes() {
        // given - WEB
        RegisterFcmTokenUseCase.Param webParam = new RegisterFcmTokenUseCase.Param(
                100L,
                "web-token",
                "WEB_PC"
        );

        // given - ANDROID
        RegisterFcmTokenUseCase.Param androidParam = new RegisterFcmTokenUseCase.Param(
                100L,
                "android-token",
                "WEB_ANDROID"
        );

        // given - IOS
        RegisterFcmTokenUseCase.Param iosParam = new RegisterFcmTokenUseCase.Param(
                100L,
                "ios-token",
                "WEB_IOS"
        );

        // when
        useCase.execute(webParam);
        useCase.execute(androidParam);
        useCase.execute(iosParam);

        // then
        verify(subscriptionRepository, times(3)).save(any(PushNotificationSubscription.class));
    }
}