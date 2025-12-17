package com.lgcns.bebee.notification.domain.service;

import com.lgcns.bebee.notification.core.exception.NotificationException;
import com.lgcns.bebee.notification.domain.entity.PushNotificationSubscription;
import com.lgcns.bebee.notification.domain.repository.PushNotificationSubscriptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PushNotificationSubscriptionManagement 단위 테스트")
class PushNotificationSubscriptionManagementTest {

    @Mock
    private PushNotificationSubscriptionRepository subscriptionRepository;

    @InjectMocks
    private PushNotificationSubscriptionManagement subscriptionManagement;

    @Test
    @DisplayName("토큰이 존재하지 않으면 검증을 통과한다 (정상 처리)")
    void success_TokenNotExists() {
        // given
        Long currentMemberId = 100L;
        String token = "new-token-1234567890";

        when(subscriptionRepository.findByToken(token)).thenReturn(Optional.empty());

        // when
        subscriptionManagement.validateSubscription(currentMemberId, token);

        // then
        verify(subscriptionRepository, times(1)).findByToken(token);
        verify(subscriptionRepository, never()).delete(any());
    }

    @Test
    @DisplayName("같은 회원이 같은 토큰을 이미 가지고 있으면 예외가 발생한다")
    void fail_SameMemberSameToken() {
        // given
        Long currentMemberId = 100L;
        String token = "existing-token-1234567890";

        PushNotificationSubscription existingSubscription = mock(PushNotificationSubscription.class);
        when(existingSubscription.getSubscriberId()).thenReturn(currentMemberId);

        when(subscriptionRepository.findByToken(token)).thenReturn(Optional.of(existingSubscription));

        // when & then
        assertThatThrownBy(() -> subscriptionManagement.validateSubscription(currentMemberId, token))
                .isInstanceOf(NotificationException.class);

        verify(subscriptionRepository, times(1)).findByToken(token);
        verify(subscriptionRepository, never()).delete(any());
    }

    @Test
    @DisplayName("다른 회원이 같은 토큰을 가지고 있으면 기존 구독을 삭제한다")
    void success_DifferentMemberSameToken_DeleteOldSubscription() {
        // given
        Long currentMemberId = 100L;
        Long otherMemberId = 200L;
        String token = "shared-token-1234567890";

        PushNotificationSubscription existingSubscription = mock(PushNotificationSubscription.class);
        when(existingSubscription.getSubscriberId()).thenReturn(otherMemberId);

        when(subscriptionRepository.findByToken(token)).thenReturn(Optional.of(existingSubscription));

        // when
        subscriptionManagement.validateSubscription(currentMemberId, token);

        // then
        verify(subscriptionRepository, times(1)).findByToken(token);
        verify(subscriptionRepository, times(1)).delete(existingSubscription);
    }
}
