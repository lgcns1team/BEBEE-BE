package com.lgcns.bebee.notification.core.config;

import com.google.firebase.ErrorCode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DisplayName("FCM 설정 및 연결 테스트")
class FcmConfigTest {

    @Autowired
    private FirebaseApp firebaseApp;

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Test
    @DisplayName("FirebaseApp 빈이 정상적으로 생성된다")
    void firebaseApp_BeanCreated() {
        // then
        assertThat(firebaseApp).isNotNull();
        assertThat(firebaseApp.getName()).isEqualTo("[DEFAULT]");
    }

    @Test
    @DisplayName("FirebaseMessaging 빈이 정상적으로 생성된다")
    void firebaseMessaging_BeanCreated() {
        // then
        assertThat(firebaseMessaging).isNotNull();
    }

    @Test
    @DisplayName("Dry-run 모드로 Firebase 서버 연결 및 인증을 확인한다")
    void firebaseConnection_DryRun_Success() {
        // given
        // 더미 FCM 토큰 (실제 디바이스가 아니므로 전송되지 않음)
        String dummyToken = "fGc1hX8kQR2Cv9m4wZ7pLN:APA91bH_fake_token_for_dry_run_test_1234567890";

        Message message = Message.builder()
                .setToken(dummyToken)
                .setNotification(Notification.builder()
                        .setTitle("Dry-run 테스트")
                        .setBody("이 메시지는 실제로 전송되지 않습니다.")
                        .build())
                .build();

        // when & then
        // Dry-run 모드 (두 번째 파라미터 true)
        // - Firebase 서버와 실제 통신하여 서비스 계정 키 인증 검증
        // - 메시지 형식 검증
        // - 실제 전송은 하지 않음
        // - 서비스 계정 키가 올바르면 예외가 발생하지 않거나, 토큰 관련 예외만 발생
        assertDoesNotThrow(() -> {
            try {
                String response = firebaseMessaging.send(message, true);
                // 성공적으로 dry-run이 완료되면 메시지 ID가 반환됨
                assertThat(response).isNotNull();
                System.out.println("Dry-run 성공: Firebase 서버 연결 및 인증 확인됨");
                System.out.println("Response: " + response);
            } catch (FirebaseMessagingException e) {
                // 토큰이 유효하지 않아도 Firebase 연결 및 인증은 성공한 것
                // INVALID_ARGUMENT, UNREGISTERED 등의 에러는 토큰 문제이지 인증 문제가 아님
                if (isAuthenticationError(e)) {
                    // 인증 오류면 실패
                    throw e;
                } else {
                    // 토큰 관련 오류는 OK (Firebase 연결은 성공한 것)
                    System.out.println("Firebase 연결 및 인증 확인됨 (토큰 관련 오류는 예상된 동작)");
                    System.out.println("Error Code: " + e.getErrorCode());
                    System.out.println("Error Message: " + e.getMessage());
                }
            }
        });
    }

    /**
     * 인증 관련 오류인지 확인합니다.
     *
     * @param e FirebaseMessagingException
     * @return 인증 오류이면 true, 토큰 관련 오류이면 false
     */
    private boolean isAuthenticationError(FirebaseMessagingException e) {
        // 인증 실패 관련 에러 코드
        // - UNAUTHENTICATED: 서비스 계정 키가 유효하지 않음
        // - PERMISSION_DENIED: 권한 부족
        ErrorCode errorCode = e.getErrorCode();
        return errorCode != null && (
                errorCode.equals(ErrorCode.UNAUTHENTICATED) ||
                        errorCode.equals(ErrorCode.PERMISSION_DENIED)
        );
    }
}