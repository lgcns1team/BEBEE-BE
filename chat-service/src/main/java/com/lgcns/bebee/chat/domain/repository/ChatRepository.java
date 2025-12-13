package com.lgcns.bebee.chat.domain.repository;

import com.lgcns.bebee.chat.domain.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * Chat 엔티티의 MongoDB Repository
 */
public interface ChatRepository extends MongoRepository<Chat, Long> {
}