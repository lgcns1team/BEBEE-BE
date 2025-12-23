package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.ChatroomHelpCategory;
import com.lgcns.bebee.chat.domain.entity.HelpCategorySync;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.service.ChatroomManagement;
import com.lgcns.bebee.chat.domain.service.MemberManagement;
import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenChatroomUseCase implements UseCase<OpenChatroomUseCase.Param, OpenChatroomUseCase.Result> {
    private final ChatroomManagement chatroomManagement;
    private final MemberManagement memberManagement;

    @Override
    @Transactional
    public Result execute(Param param) {
        // 현재 사용자 조회
        MemberSync currentMember = memberManagement.getExistingMember(param.currentMemberId);

        if(param.chatroomId != null){
            Chatroom chatroom = chatroomManagement.findChatroomWithMembers(param.chatroomId);
            MemberSync otherMember = getOtherMember(chatroom, param.currentMemberId);

            return new Result(
                    chatroom.getId(), param.currentMemberId, otherMember.getId(),
                    otherMember.getNickname(), otherMember.getProfileImageUrl(), otherMember.getSweetness(),
                    null
            );
        }

        // 상대방 조회
        MemberSync otherMember = memberManagement.getExistingMember(param.otherMemberId);

        Chatroom chatroom = chatroomManagement.findChatroomWithMembers(currentMember, otherMember);
        otherMember = getOtherMember(chatroom, param.currentMemberId);

        chatroomManagement.linkPost(chatroom, param.postId, param.postTitle, param.helpCategoryIds);

        List<HelpCategorySync> helpCategories = chatroom.getChatroomHelpCategories().stream()
                .map(ChatroomHelpCategory::getHelpCategory)
                .toList();

        return new Result(
                chatroom.getId(),
                param.currentMemberId,
                otherMember.getId(),
                otherMember.getNickname(),
                otherMember.getProfileImageUrl(),
                otherMember.getSweetness(),
                helpCategories
        );
    }

    private MemberSync getOtherMember(Chatroom chatroom, Long currentMemberId) {
        boolean isCurrentUserMember1 = chatroom.getMember1().getId().equals(currentMemberId);
        return isCurrentUserMember1 ? chatroom.getMember2() : chatroom.getMember1();
    }

    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long chatroomId;
        private final Long currentMemberId;
        private final Long otherMemberId;
        private final Long postId;
        private final String postTitle;
        private final List<Long> helpCategoryIds;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result {
        private final Long chatroomId;
        private final Long myId;
        private final Long otherId;
        private final String otherNickname;
        private final String otherProfileImageUrl;
        private final BigDecimal otherSweetness;
        private final List<HelpCategorySync> helpCategories;
    }
}