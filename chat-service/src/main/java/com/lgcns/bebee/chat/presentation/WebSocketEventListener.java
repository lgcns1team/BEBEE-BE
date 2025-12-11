package com.lgcns.bebee.chat.presentation;

import com.lgcns.bebee.chat.application.SubscribeChannelOnConnectionUseCase;
import com.lgcns.bebee.chat.application.UnsubscribeChannelOnConnectionUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SubscribeChannelOnConnectionUseCase subscribeChannelUseCase;
    private final UnsubscribeChannelOnConnectionUseCase unsubscribeChannelUseCase;

    /**
     * 클라이언트가 STOMP CONNECT 프레임을 전송했을 때 호출됩니다.
     * WebSocket 인증이 완료된 후 해당 회원의 채널을 구독합니다.
     * SessionConnectEvent는 STOMP CONNECT 프레임을 수신했을 때 발생합니다.
     *
     * @param event STOMP CONNECT 이벤트
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = headerAccessor.getUser();

        if (principal != null) {
            Long memberId = Long.parseLong(principal.getName());
            log.info("WebSocket connected - Member ID: {}, Session ID: {}",
                    memberId, headerAccessor.getSessionId());

            subscribeChannelUseCase.execute(new SubscribeChannelOnConnectionUseCase.Param(memberId));
        } else {
            log.warn("WebSocket connected without member authentication - Session ID: {}",
                    headerAccessor.getSessionId());
        }
    }

    /**
     * WebSocket 연결이 해제되면 해당 회원의 채널 구독을 해제합니다.
     *
     * @param event WebSocket 연결 해제 이벤트
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = headerAccessor.getUser();

        if (principal != null) {
            Long memberId = Long.parseLong(principal.getName());
            log.info("WebSocket disconnected - Member ID: {}, Session ID: {}",
                    memberId, headerAccessor.getSessionId());

            unsubscribeChannelUseCase.execute(new  UnsubscribeChannelOnConnectionUseCase.Param(memberId));
        }
    }

}