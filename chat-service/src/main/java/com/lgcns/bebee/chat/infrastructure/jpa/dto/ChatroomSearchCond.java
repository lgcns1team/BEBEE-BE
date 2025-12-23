package com.lgcns.bebee.chat.infrastructure.jpa.dto;

import com.lgcns.bebee.chat.domain.entity.MemberSync;

/**
 * Chatroom 조회 조건을 담는 DTO
 * - chatroomId로 조회하거나
 * - member1Id, member2Id로 조회
 */
public record ChatroomSearchCond(
        Long chatroomId,
        Long member1Id,
        Long member2Id
) {
    public static ChatroomSearchCond of(Long chatroomId){
        
        return new ChatroomSearchCond(
                chatroomId,
                null, null
        );
    }

    public static ChatroomSearchCond of(MemberSync member1, MemberSync member2){
        return new ChatroomSearchCond(
                null,
                member1.getId(), member2.getId()
        );
    }
}