package com.lgcns.bebee.chat.domain.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class LastReadChat {
    @Id
    @Tsid
    private Long id;

    @Field(name = "chatroom_id")
    private Long chatRoomId;

    @Field(name = "reader_id")
    private Long readerId;

    @Field(name = "last_read_chat_id")
    private Long lastReadChatId;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "updated_at")
    private LocalDateTime updatedAt;
}
