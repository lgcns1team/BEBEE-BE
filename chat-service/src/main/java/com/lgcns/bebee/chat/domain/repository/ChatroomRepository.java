package com.lgcns.bebee.chat.domain.repository;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository {

    Chatroom save(MemberSync member1, MemberSync member2);

    Optional<Chatroom> findById(Long chatroomId);

    Optional<Chatroom> findChatroom(MemberSync member1, MemberSync member2);

    Optional<Chatroom> findChatroomWithMembers(Long chatroomId);

    Optional<Chatroom> findChatroomWithMembers(MemberSync member1, MemberSync member2);

    List<Chatroom> findChatroomsWithCursor(MemberSync member, Long lastChatroomId, int limit);
}
