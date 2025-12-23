package com.lgcns.bebee.chat.domain.service;

import com.lgcns.bebee.chat.core.exception.ChatErrors;
import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.HelpCategorySync;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.repository.ChatroomRepository;
import com.lgcns.bebee.chat.domain.repository.HelpCategorySyncRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatroomManagement {
    private final ChatroomRepository chatroomRepository;
    private final HelpCategorySyncRepository helpCategorySyncRepository;

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
     * ID로 채팅방과 멤버 정보를 함께 조회합니다.
     *
     * @param chatroomId 조회할 채팅방 ID
     * @return 멤버 정보가 포함된 채팅방 엔티티
     * @throws com.lgcns.bebee.chat.core.exception.ChatException 채팅방이 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public Chatroom findChatroomWithMembers(Long chatroomId){
        return chatroomRepository.findChatroomWithMembers(chatroomId)
                .orElseThrow(ChatErrors.CHATROOM_NOT_FOUND::toException);
    }

    /**
     * 두 멤버 간의 채팅방을 조회하거나 없으면 새로 생성합니다.
     * 기존 채팅방이 존재하면 해당 채팅방을 반환하고,
     * 없으면 새로운 채팅방을 생성하여 반환합니다.
     *
     * @param currentMember 현재 사용자
     * @param otherMember 상대방 사용자
     * @return 기존 또는 새로 생성된 채팅방 엔티티
     */
    @Transactional
    public Chatroom findChatroomWithMembers(MemberSync currentMember, MemberSync otherMember) {
        return chatroomRepository.findChatroomWithMembers(currentMember, otherMember)
                .orElseGet(() ->
                        chatroomRepository.save(currentMember, otherMember));
    }

    /**
     * 채팅방에 게시글을 연결하고 도움 카테고리를 설정합니다.
     *
     * @param chatroom 채팅방
     * @param postId 게시글 ID
     * @param postTitle 게시글 제목
     * @param helpCategoryIds 도움 카테고리 ID 목록
     */
    @Transactional
    public void linkPost(Chatroom chatroom, Long postId, String postTitle, List<Long> helpCategoryIds){
        List<HelpCategorySync> helpCategories = null;
        if(helpCategoryIds != null && !helpCategoryIds.isEmpty()) helpCategories = helpCategorySyncRepository.findByIds(helpCategoryIds);

        chatroom.linkPost(postId, postTitle, helpCategories);
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