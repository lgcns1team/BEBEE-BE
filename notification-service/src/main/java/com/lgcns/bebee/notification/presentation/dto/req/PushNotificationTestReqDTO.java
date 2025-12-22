package com.lgcns.bebee.notification.presentation.dto.req;

public record PushNotificationTestReqDTO(
        Long senderId,
        Long receiverId,
        String notificationType,
        Long applicationId,
        Long matchId,
        Long chatroomId,
        String messagePreview
) {
}