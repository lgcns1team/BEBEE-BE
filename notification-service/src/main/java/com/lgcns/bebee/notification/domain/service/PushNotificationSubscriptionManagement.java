package com.lgcns.bebee.notification.domain.service;

import com.lgcns.bebee.notification.core.exception.NotificationErrors;
import com.lgcns.bebee.notification.domain.entity.PushNotificationSubscription;
import com.lgcns.bebee.notification.domain.repository.PushNotificationSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PushNotificationSubscriptionManagement {
    private final PushNotificationSubscriptionRepository subscriptionRepository;

    /**
     * FCM 토큰 등록 시 유효성을 검증하고 토큰 재할당을 처리합니다.
     * 
     * @param currentMemberId 현재 토큰을 등록하려는 회원 ID
     * @param token FCM 토큰
     * @throws com.lgcns.bebee.notification.core.exception.NotificationException 같은 회원이 같은 토큰을 재등록하려는 경우
     */
    @Transactional
    public void validateSubscription(Long currentMemberId, String token) {
        subscriptionRepository.findByToken(token).ifPresent(subscription -> {
            if(currentMemberId.equals(subscription.getSubscriberId())){
                throw NotificationErrors.TOKEN_ALREADY_EXISTS.toException();
            }

            // 해당 토큰을 다른 사람의 소유라면 해당 토큰을 무효화한다.
            subscriptionRepository.delete(subscription);
        });
    }


    /**
     * 회원의 푸시 알림 구독 정보를 조회합니다.
     *
     * @param currentMemberId 조회할 회원 ID
     * @return 푸시 알림 구독 정보 (FCM 토큰 포함)
     * @throws com.lgcns.bebee.notification.core.exception.NotificationException 회원의 구독 정보가 존재하지 않는 경우
     */
    @Transactional(readOnly = true)
    public PushNotificationSubscription getExistingPushNotificationSubscription(Long currentMemberId) {
        return subscriptionRepository.findBySubscriberId(currentMemberId).orElseThrow(NotificationErrors.SUBSCRIPTION_NOT_FOUND::toException);
    }
}
