package com.lgcns.bebee.chat.domain.service;

import com.lgcns.bebee.chat.core.exception.ChatErrors;
import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatroomManagement {
    private final ChatroomRepository chatroomRepository;

    /**
     * ID로 채팅방을 조회합니다.
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
     * 채팅방을 조회합니다.
     * - chatroomId로 조회하거나
     * - member1Id, member2Id로 조회
     *
     * @param chatroomId 채팅방 ID (nullable)
     * @param currentMemberId 현재 회원 ID (nullable)
     * @param otherMemberId 상대 회원 ID (nullable)
     * @return 조회된 채팅방 엔티티
     * @throws com.lgcns.bebee.chat.core.exception.ChatException 채팅방이 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public Chatroom findChatroom(Long chatroomId, Long currentMemberId, Long otherMemberId) {
        return chatroomRepository.findChatroomWithMembers(chatroomId, currentMemberId, otherMemberId)
                .orElseThrow(ChatErrors.CHATROOM_NOT_FOUND::toException);
    }

    /**
     * 채팅방을 열거나 생성합니다.
     * - chatroomId가 있으면 해당 채팅방을 조회합니다.
     * - chatroomId가 없으면 sender와 receiver로 채팅방을 찾거나 새로 생성합니다.
     *
     * @param chatroomId 기존 채팅방 ID (nullable)
     * @param sender 발신자
     * @param receiver 수신자
     * @return 조회되거나 생성된 채팅방
     */
    @Transactional
    public Chatroom openChatroom(Long chatroomId, MemberSync sender, MemberSync receiver) {
        // chatroomId가 있으면 기존 채팅방 조회
        if (chatroomId != null) {
            return getExistingChatroom(chatroomId);
        }

        // sender와 receiver로 채팅방을 찾거나 생성
        return chatroomRepository.findChatroom(sender, receiver)
                .orElseGet(() ->
                    chatroomRepository.save(sender, receiver));
    }

    /**
     * 채팅방의 마지막 메시지를 업데이트합니다.
     *
     * @param chatroom 업데이트할 채팅방
     * @param chat 새로운 채팅 메시지
     */
    @Transactional
    public void updateLastMessage(Chatroom chatroom, Chat chat) {
        String lastMessage = determineLastMessage(chat);

        // MATCH_SUCCESS, MATCH_FAILURE는 업데이트하지 않음
        if (lastMessage != null) {
            chatroom.updateLastMessage(lastMessage);
        }
    }

    /**
     * 채팅 타입에 따라 저장할 마지막 메시지를 결정합니다.
     *
     * @param chat 채팅 메시지
     * @return 저장할 마지막 메시지 (MATCH_SUCCESS, MATCH_FAILURE인 경우 null)
     */
    private String determineLastMessage(Chat chat) {
        return switch (chat.getType()) {
            case TEXT -> {
                // TEXT 타입: 32자로 제한
                String textContent = chat.getTextContent();
                if (textContent == null || textContent.isEmpty()) {
                    yield "";
                }
                yield textContent.length() > 31
                        ? textContent.substring(0, 31)
                        : textContent;
            }
            case IMAGE -> "(이미지)";
            case MATCH_CONFIRMATION -> "(매칭 확인서)";
            case MATCH_SUCCESS, MATCH_FAILURE -> null; // 업데이트하지 않음
        };
    }
}