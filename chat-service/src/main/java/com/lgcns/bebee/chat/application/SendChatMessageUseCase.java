package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.application.client.MessagePublisher;
import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.service.ChatroomManagement;
import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SendChatMessageUseCase implements UseCase<SendChatMessageUseCase.Param, Void> {
    private final MessagePublisher messagePublisher;
    private final ChatroomManagement chatroomManagement;

    @Override
    @Transactional
    public Void execute(Param param) {
        Chatroom chatroom = chatroomManagement.openChatroom(param.chatroomId, param.senderId, param.receiverId);

        Chat chat = Chat.create(
                param.chatroomId,
                param.senderId,
                param.textContent,
                param.chatType,
                param.attachments,
                param.agreementId, param.matchType, param.startDate, param.endDate,
                param.scheduleDays, param.scheduleStartTimes, param.scheduleEndTimes,
                param.location,
                param.unitPoints, param.totalPoints,
                param.matchStatus,
                param.createdAt
        );

        // Redis를 통해 발신자와 수신자에게 메시지 발행
        messagePublisher.publishToMember(param.senderId, param.receiverId, chat);

        return null;
    }

    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long chatroomId;
        private final Long senderId;
        private final Long receiverId;
        private final String textContent;
        private final String chatType;
        private final List<String> attachments;
        private final Long agreementId;
        private final String matchType;
        private final String startDate;
        private final String endDate;
        private final List<String> scheduleDays;
        private final List<String> scheduleStartTimes;
        private final List<String> scheduleEndTimes;
        private final String location;
        private final Integer unitPoints;
        private final Integer totalPoints;
        private final String matchStatus;
        private final LocalDateTime createdAt;
    }
}