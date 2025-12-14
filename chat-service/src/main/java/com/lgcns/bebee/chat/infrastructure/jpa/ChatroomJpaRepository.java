package com.lgcns.bebee.chat.infrastructure.jpa;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatroomJpaRepository extends JpaRepository<Chatroom, Long> {

    @Query("SELECT c FROM Chatroom c WHERE c.member1 = :member1 and c.member2 = :member2")
    Optional<Chatroom> findChatroom(MemberSync member1, MemberSync member2);
}
