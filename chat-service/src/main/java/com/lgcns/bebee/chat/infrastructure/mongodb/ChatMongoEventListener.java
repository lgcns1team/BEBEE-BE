package com.lgcns.bebee.chat.infrastructure.mongodb;

import com.lgcns.bebee.chat.domain.entity.Chat;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Chat 엔티티 저장 전 TSID를 자동 생성하는 MongoDB Event Listener
 */
@Component
@RequiredArgsConstructor
public class ChatMongoEventListener extends AbstractMongoEventListener<Chat> {
    private final TSID.Factory tsidFactory;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Chat> event) {
        Chat chat = event.getSource();

        try {
            // Reflection을 사용하여 id 필드에 접근
            Field idField = Chat.class.getDeclaredField("id");
            idField.setAccessible(true);

            // id가 null인 경우에만 TSID 생성
            if (idField.get(chat) == null) {
                Long tsid = tsidFactory.generate().toLong();
                idField.set(chat, tsid);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to generate TSID for Chat entity", e);
        }
    }
}