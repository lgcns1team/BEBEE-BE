package com.lgcns.bebee.chat.domain.repository;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    @Query("SELECT c FROM Chatroom c WHERE c.member1Id = :member1Id and c.member2Id = :member2Id")
    Optional<Chatroom> findChatroom(Long member1Id, Long member2Id);
}
