package com.lgcns.bebee.notification.presentation;

import com.lgcns.bebee.notification.application.SendPushNotificationUseCase;
import com.lgcns.bebee.notification.presentation.dto.req.PushNotificationTestReqDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/notifications/test")
@RequiredArgsConstructor
public class NotificationTestController {
    private final SendPushNotificationUseCase sendPushNotificationUseCase;
    
    @PostMapping("/push")
    public void sendTestPushNotification(
            @RequestBody PushNotificationTestReqDTO reqDTO
    ) {
        SendPushNotificationUseCase.Param param = new SendPushNotificationUseCase.Param(
                reqDTO.senderId(),
                reqDTO.receiverId(),
                reqDTO.notificationType(),
                reqDTO.applicationId(),
                reqDTO.matchId(),
                reqDTO.chatroomId(),
                reqDTO.messagePreview()
        );

        sendPushNotificationUseCase.execute(param);
    }
}