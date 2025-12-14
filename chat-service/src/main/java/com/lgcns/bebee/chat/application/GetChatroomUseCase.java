package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.service.ChatroomManagement;
import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class GetChatroomUseCase implements UseCase<GetChatroomUseCase.Param, GetChatroomUseCase.Result> {
    private final ChatroomManagement chatroomManagement;

    @Override
    public Result execute(Param param) {
        Chatroom chatroom = chatroomManagement.findChatroom(param.chatroomId, param.currentMemberId, param.otherMemberId);

        // 현재 사용자와 상대방 구분
        boolean isCurrentUserMember1 = chatroom.getMember1().getId().equals(param.currentMemberId);
        Long myId = param.currentMemberId;
        MemberSync otherMember = isCurrentUserMember1
            ? chatroom.getMember2()
            : chatroom.getMember1();

        return new Result(chatroom.getId(), myId,
                otherMember.getId(), otherMember.getNickname(), otherMember.getProfileImageUrl(), otherMember.getSweetness());
    }

    @RequiredArgsConstructor
    public static class Param implements Params{
        private final Long chatroomId;
        private final Long currentMemberId;
        private final Long otherMemberId;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result{
        private final Long chatroomId;
        private final Long myId;
        private final Long otherId;
        private final String otherNickname;
        private final String otherProfileImageUrl;
        private final BigDecimal otherSweetness;
    }
}
