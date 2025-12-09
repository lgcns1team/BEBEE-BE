package com.lgcns.bebee.chat.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lgcns.bebee.chat.infrastructure.redis.dto.ChatMessage;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.lgcns.bebee.chat.infrastructure.redis.RedisUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisPubSubAsyncCommands<String, String> asyncCommands;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 특정 채팅방에 메시지를 발행합니다.
     *
     * @param chatroomId 채팅방 ID
     * @param message 발행할 채팅 메시지
     */
    public void publishToChatroom(Long chatroomId, ChatMessage message) {
        try {
            String channel = getChatroomChannel(chatroomId);
            String messageJson = objectMapper.writeValueAsString(message);
            asyncCommands.publish(channel, messageJson)
                    .thenAccept(receivers ->
                        log.info("Published message to channel: {}, receivers: {}", channel, receivers))
                    .exceptionally(throwable -> {
                        log.error("Failed to publish message to channel: {}", channel, throwable);
                        return null;
                    });
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize chat message", e);
        }
    }
}