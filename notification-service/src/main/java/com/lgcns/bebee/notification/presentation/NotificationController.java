package com.lgcns.bebee.notification.presentation;

import com.lgcns.bebee.notification.application.RegisterFcmTokenUseCase;
import com.lgcns.bebee.notification.presentation.dto.req.FcmTokenRegisterReqDTO;
import com.lgcns.bebee.notification.presentation.swagger.NotificationSwagger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController implements NotificationSwagger {
    private RegisterFcmTokenUseCase registerFcmTokenUseCase;

    @PostMapping("/fcm/tokens")
    public ResponseEntity<Void> registerToken(
            @RequestParam Long memberId,
            @RequestBody FcmTokenRegisterReqDTO reqDTO
            ){
        RegisterFcmTokenUseCase.Param param = new RegisterFcmTokenUseCase.Param(memberId, reqDTO.token(), reqDTO.deviceType());

        registerFcmTokenUseCase.execute(param);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
