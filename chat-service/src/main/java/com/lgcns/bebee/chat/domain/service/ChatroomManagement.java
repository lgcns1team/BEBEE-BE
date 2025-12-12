package com.lgcns.bebee.chat.domain.service;

import com.lgcns.bebee.chat.core.exception.ChatErrors;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatroomManagement {
    private final ChatroomRepository chatroomRepository;

    /**
     * 기존 채팅방을 조회합니다.
     *
     * @param chatroomId 조회할 채팅방 ID
     * @return 조회된 채팅방 엔티티
     * @throws com.lgcns.bebee.chat.core.exception.ChatException 채팅방이 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public Chatroom getExistingChatroom(Long chatroomId){
        return chatroomRepository.findById(chatroomId).orElseThrow(ChatErrors.CHATROOM_NOT_FOUND::toException);
    }

    /**
     * 채팅방을 검증하거나 생성합니다.
     *
     * @param chatroomId 기존 채팅방 ID (nullable)
     * @param senderId 발신자 ID
     * @param receiverId 수신자 ID
     * @return 검증되거나 생성된 채팅방
     */
    @Transactional
    public Chatroom openChatroom(Long chatroomId, Long senderId, Long receiverId) {
        // chatroomId가 있으면 Chatroom 조회
        if (chatroomId != null) {
            return getExistingChatroom(chatroomId);
        }

        // chatroomId가 없으면 senderId와 receiverId로 채팅방 조회 또는 생성
        // member1은 항상 작은 ID, member2는 큰 ID로 정렬하여 일관성 유지
        Long member1 = Math.min(senderId, receiverId);
        Long member2 = Math.max(senderId, receiverId);

        return chatroomRepository.findChatroom(member1, member2)
                .orElseGet(() -> Chatroom.create(member1, member2));
    }
}