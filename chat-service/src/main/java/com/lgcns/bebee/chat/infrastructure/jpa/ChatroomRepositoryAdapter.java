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
    public Optional<Chatroom> findChatroomWithMembers(Long chatroomId, Long member1Id, Long member2Id){
        if(chatroomId != null){
            ChatroomSearchCond cond = ChatroomSearchCond.of(chatroomId);
            return queryDSLChatroomRepository.findChatroomWithMembers(cond);
        }

        // member1Id은 항상 작은 ID, member2Id는 큰 ID로 정렬하여 일관성 유지
        if (member1Id != null && member2Id != null) {
            Long normalizedMember1Id = Math.min(member1Id, member2Id);
            Long normalizedMember2Id = Math.max(member1Id, member2Id);

            ChatroomSearchCond cond = ChatroomSearchCond.of(normalizedMember1Id, normalizedMember2Id);
            return queryDSLChatroomRepository.findChatroomWithMembers(cond);
        }

        return Optional.empty();
    }

    @Override
    public List<Chatroom> findChatroomsWithCursor(MemberSync member, Long lastChatroomId, int limit) {
        return chatroomJpaRepository.findByMemberWithCursor(member, lastChatroomId, Limit.of(limit));
    }
}
