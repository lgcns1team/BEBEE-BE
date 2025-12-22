package com.lgcns.bebee.chat.presentation;

import com.lgcns.bebee.chat.application.SendChatMessageUseCase;
import com.lgcns.bebee.chat.presentation.dto.req.ChatMessageSendReqDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatStreamController {
    private final SendChatMessageUseCase sendChatMessageUseCase;

    /**
     * 클라이언트로부터 채팅 메시지를 수신하고 메시지 채널을 통해 발행합니다.
     *
     * @param request 채팅 메시지 요청
     * @param principal WebSocket 인증을 통해 주입된 MemberPrincipal 객체 (회원 ID 포함)
     */
    @MessageMapping("/chats")
    public void sendMessage(@Payload ChatMessageSendReqDTO request, Principal principal) {
        Long senderId = Long.parseLong(principal.getName());

        SendChatMessageUseCase.Param param = new SendChatMessageUseCase.Param(
                request.chatroomId(),
                senderId,
                request.receiverId(),
                request.textContent(),
                request.chatType(),
                request.attachments(),
                request.agreementId(), request.matchType(), request.startDate(), request.endDate(),
                request.scheduleDays(), request.scheduleStartTimes(), request.scheduleEndTimes(),
                request.location(),
                request.unitPoints(), request.totalPoints(),
                request.matchStatus(),
                request.createdAt()
        );

        sendChatMessageUseCase.execute(param);
    }
}