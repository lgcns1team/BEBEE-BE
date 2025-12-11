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
public class RedisMessagePublisher {
    private final RedisPubSubAsyncCommands<String, String> asyncCommands;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 1:1 채팅 메시지를 발행합니다.
     * 발신자와 수신자 모두에게 메시지를 전송하여 멀티 디바이스를 지원합니다.
     *
     * @param senderId 발신자 ID
     * @param receiverId 수신자 ID
     * @param chat 전송할 채팅 도메인 객체
     */
    public void publishToMember(Long senderId, Long receiverId, Chat chat) {
        try {
            ChatMessageDTO chatMessageDTO = ChatMessageDTO.from(chat);

            String messageJson = objectMapper.writeValueAsString(chatMessageDTO);

            // 수신자에게 메시지 발행
            publishToChannel(getMemberChannel(receiverId), messageJson);

            // 발신자에게도 메시지 발행 (멀티 디바이스 지원)
            publishToChannel(getMemberChannel(senderId), messageJson);

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize chat message", e);
        }
    }

    private void publishToChannel(String channel, String messageJson) {
        asyncCommands.publish(channel, messageJson)
                .thenAccept(receivers ->
                        log.info("Published message to channel: {}, receivers: {}", channel, receivers))
                .exceptionally(throwable -> {
                    log.error("Failed to publish message to channel: {}", channel, throwable);
                    return null;
                });
    }
}