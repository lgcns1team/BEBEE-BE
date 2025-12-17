package com.lgcns.bebee.notification.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "FCM 토큰 등록 요청")
public record FcmTokenRegisterReqDTO(
        @Schema(
                description = "Firebase에서 받은 FCM 토큰",
                example = "dX3kL9mN2Pq:APA91bH_1234567890abcdefghijklmnopqrstuvwxyz",
                required = true
        )
        String token,

        @Schema(
                description = "디바이스 타입",
                example = "WEB_PC",
                allowableValues = {"WEB_PC", "WEB_ANDROID", "WEB_IOS"},
                required = true
        )
        String deviceType
) {
}
