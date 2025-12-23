package com.lgcns.bebee.chat.infrastructure.jpa;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.repository.ChatroomRepository;
import com.lgcns.bebee.chat.infrastructure.jpa.dto.ChatroomSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatroomRepositoryAdapter implements ChatroomRepository {
    private final ChatroomJpaRepository chatroomJpaRepository;
    private final QueryDSLChatroomRepository queryDSLChatroomRepository;

    @Override
    public Chatroom save(MemberSync member1, MemberSync member2){
        MemberSync normalizedMember1 = member1.getId() < member2.getId() ? member1 : member2;
        MemberSync normalizedMember2 = member1.getId() < member2.getId() ? member2 : member1;

        Chatroom chatroom = Chatroom.create(normalizedMember1, normalizedMember2);

        return chatroomJpaRepository.save(chatroom);
    }

    @Override
    public Optional<Chatroom> findById(Long chatroomId){
        return chatroomJpaRepository.findById(chatroomId);
    }

    @Override
    public Optional<Chatroom> findChatroom(MemberSync member1, MemberSync member2){
        MemberSync normalizedMember1 = member1.getId() < member2.getId() ? member1 : member2;
        MemberSync normalizedMember2 = member1.getId() < member2.getId() ? member2 : member1;

        return chatroomJpaRepository.findChatroom(normalizedMember1, normalizedMember2);
    }

    @Override
    public Optional<Chatroom> findChatroomWithMembers(Long chatroomId) {
        ChatroomSearchCond cond = ChatroomSearchCond.of(chatroomId);
        return queryDSLChatroomRepository.findChatroomWithMembers(cond);
    }

    @Override
    public Optional<Chatroom> findChatroomWithMembers(MemberSync member1, MemberSync member2) {
        MemberSync normalizedMember1 = member1.getId() < member2.getId() ? member1 : member2;
        MemberSync normalizedMember2 = member1.getId() < member2.getId() ? member2 : member1;

        ChatroomSearchCond cond = ChatroomSearchCond.of(normalizedMember1, normalizedMember2);
        return queryDSLChatroomRepository.findChatroomWithMembers(cond);
    }

    @Override
    public List<Chatroom> findChatroomsWithCursor(MemberSync member, Long lastChatroomId, int limit) {
        return chatroomJpaRepository.findByMemberWithCursor(member, lastChatroomId, Limit.of(limit));
    }
}
