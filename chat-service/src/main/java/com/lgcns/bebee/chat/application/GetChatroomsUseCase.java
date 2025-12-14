package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.repository.ChatroomRepository;
import com.lgcns.bebee.chat.domain.service.MemberManagement;
import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetChatroomsUseCase implements UseCase<GetChatroomsUseCase.Param, GetChatroomsUseCase.Result> {
    private final MemberManagement memberManagement;
    private final ChatroomRepository chatroomRepository;

    @Override
    public Result execute(Param param) {
        MemberSync currentMember = memberManagement.getExistingMember(param.currentMemberId);

        // lastChatroomId가 null이면 Long.MAX_VALUE 사용 (쿼리 최적화)
        Long cursorId = param.lastChatroomId != null ? param.lastChatroomId : Long.MAX_VALUE;

        // limit + 1개 조회하여 hasNext 계산
        List<Chatroom> chatrooms = chatroomRepository.findChatroomsWithCursor(
                currentMember,
                cursorId,
                param.count + 1
        );

        // 다음 페이지 존재하는 지 여부 확인
        boolean hasNext = chatrooms.size() > param.count;

        Long nextChatroomId = null;
        if(hasNext){
            nextChatroomId = chatrooms.get(param.count).getId();
            chatrooms = chatrooms.subList(0, param.count);
        }

        return new Result(hasNext, nextChatroomId, chatrooms);
    }

    @RequiredArgsConstructor
    public static class Param implements Params{
        private final Long currentMemberId;
        private final Long lastChatroomId;
        private final Integer count;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result{
        private final Boolean hasNext;
        private final Long nextChatroomId;
        private final List<Chatroom> chatrooms;
    }
}