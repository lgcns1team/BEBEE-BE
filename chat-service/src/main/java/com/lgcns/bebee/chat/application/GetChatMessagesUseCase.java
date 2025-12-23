package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.repository.ChatRepository;
import com.lgcns.bebee.chat.domain.service.ChatroomManagement;
import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetChatMessagesUseCase implements UseCase<GetChatMessagesUseCase.Param, GetChatMessagesUseCase.Result> {
    private final ChatroomManagement chatroomManagement;
    private final ChatRepository chatRepository;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Param param) {
        Chatroom chatroom = chatroomManagement.getExistingChatroom(param.chatroomId);

        // lastChatId가 null이면 Long.MAX_VALUE 사용 (처음 조회)
        Long lastChatId = param.lastChatId != null ? param.lastChatId : Long.MAX_VALUE;

        // count + 1개 조회하여 다음 페이지 존재 여부 확인
        List<Chat> chatMessages = chatRepository.findByChatroomIdAndIdLessThanOrderByIdDesc(
                chatroom.getId(), lastChatId, Limit.of(param.count + 1));

        // 다음 페이지 존재 여부 확인
        boolean hasNext = chatMessages.size() > param.count;
        Long nextChatId = null;

        if(hasNext){
            nextChatId = chatMessages.get(param.count).getId();
            chatMessages = chatMessages.subList(0, param.count);
        }

        return new Result(hasNext, nextChatId, chatMessages);
    }

    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long chatroomId;
        private final Long lastChatId;
        private final Integer count;
    }
    @Getter
    @RequiredArgsConstructor
    public static class Result {
        private final boolean hasNext;
        private final Long nextChatId;
        private final List<Chat> messages;
    }
}