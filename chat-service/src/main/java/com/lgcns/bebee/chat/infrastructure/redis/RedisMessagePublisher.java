package com.lgcns.bebee.chat.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lgcns.bebee.chat.application.client.MessagePublisher;
import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.infrastructure.dto.ChatMessageDTO;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.lgcns.bebee.chat.infrastructure.redis.RedisUtils.getMemberChannel;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMessagePublisher implements MessagePublisher {
    private final RedisPubSubAsyncCommands<String, String> asyncCommands;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 1:1 채팅 메시지를 발행합니다.
     * 발신자와 수신자 모두에게 메시지를 전송하여 멀티 디바이스를 지원합니다.
     * 각 클라이언트는 상대방의 ID를 receiverId 필드로 받습니다.
     *
     * @param senderId 발신자 ID
     * @param receiverId 수신자 ID
     * @param chat 전송할 채팅 도메인 객체
     */
    public void publishToMember(Long senderId, Long receiverId, Chat chat) {
        try {
            // 수신자에게 보낼 메시지 - receiverId는 발신자(상대방)
            ChatMessageDTO messageForReceiver = ChatMessageDTO.from(receiverId, chat);
            String messageForReceiverJson = objectMapper.writeValueAsString(messageForReceiver);
            publishToChannel(getMemberChannel(receiverId), messageForReceiverJson);

            // 발신자에게 보낼 메시지 - receiverId는 수신자(상대방)
            ChatMessageDTO messageForSender = ChatMessageDTO.from(senderId, chat);
            String messageForSenderJson = objectMapper.writeValueAsString(messageForSender);
            publishToChannel(getMemberChannel(senderId), messageForSenderJson);

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