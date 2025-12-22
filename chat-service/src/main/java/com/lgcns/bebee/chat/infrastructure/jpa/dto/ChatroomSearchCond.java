package com.lgcns.bebee.chat.infrastructure.jpa.dto;

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

    public static ChatroomSearchCond of(Long member1Id, Long member2Id){
        return new ChatroomSearchCond(
                null,
                member1Id, member2Id
        );
    }
}