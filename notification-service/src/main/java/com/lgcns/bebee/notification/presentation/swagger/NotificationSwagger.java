package com.lgcns.bebee.notification.presentation.swagger;

import com.lgcns.bebee.notification.presentation.dto.req.FcmTokenRegisterReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "알림", description = "알림 관련 API")
public interface NotificationSwagger {

    @Operation(
            summary = "FCM 토큰 등록",
            description = """
                    클라이언트에서 받은 FCM 토큰을 서버에 등록합니다.

                    - 웹/앱에서 Firebase SDK의 getToken()으로 받은 토큰을 전송합니다.
                    - 같은 회원이 같은 토큰을 재등록하면 예외가 발생합니다.
                    - 다른 회원이 같은 토큰을 등록하면 이전 회원의 구독이 삭제됩니다. (토큰 재할당)
                    - 등록된 토큰은 푸시 알림 전송 시 사용됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "FCM 토큰 등록 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (토큰 또는 디바이스 타입 누락)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "같은 회원이 같은 토큰을 재등록 시도 (중복)",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<Void> registerToken(
            @Parameter(
                    description = "회원 ID (임시 용도, 나중에 JWT 토큰에서 추출)",
                    required = true,
                    example = "100"
            )
            @RequestParam Long memberId,

            @Parameter(
                    description = """
                            FCM 토큰 정보
                            - token: Firebase에서 받은 FCM 토큰
                            - deviceType: 디바이스 타입 (WEB_PC, WEB_ANDROID, WEB_IOS)
                            """,
                    required = true
            )
            @RequestBody FcmTokenRegisterReqDTO reqDTO
    );
}
