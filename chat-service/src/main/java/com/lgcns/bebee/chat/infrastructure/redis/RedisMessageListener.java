package com.lgcns.bebee.chat.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lgcns.bebee.chat.infrastructure.redis.dto.ChatMessage;
import io.lettuce.core.pubsub.RedisPubSubListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import static com.lgcns.bebee.chat.core.utils.StompDestinationUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMessageListener implements RedisPubSubListener<String, String> {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Redis 채널에서 메시지를 수신했을 때 호출됩니다.
     *
     * @param channel 메시지를 수신한 채널명
     * @param message 수신한 메시지 (JSON 형식)
     */
    @Override
    public void message(String channel, String message) {
        try {
            ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);
            log.info("Received message from channel: {}, message: {}", channel, chatMessage);

            // WebSocket을 통해 구독자들에게 메시지 전송
            String destination = toDestination(channel);
            messagingTemplate.convertAndSend(destination, chatMessage);

            log.debug("Sent message to WebSocket destination: {}", destination);
        } catch (Exception e) {
            log.error("Failed to process message from channel: {}", channel, e);
        }
    }

    @Override
    public void message(String pattern, String channel, String message) {
    }

    /**
     * Redis 채널 구독이 완료되었을 때 호출됩니다.
     *
     * @param channel 구독한 채널명
     * @param count 현재 구독 중인 채널 수
     */
    @Override
    public void subscribed(String channel, long count) {
        log.info("Subscribed to channel: {}, total subscriptions: {}", channel, count);
    }

    @Override
    public void psubscribed(String pattern, long count) {
    }

    /**
     * Redis 채널 구독 해제가 완료되었을 때 호출됩니다.
     *
     * @param channel 구독 해제한 채널명
     * @param count 남은 구독 채널 수
     */
    @Override
    public void unsubscribed(String channel, long count) {
        log.info("Unsubscribed from channel: {}, remaining subscriptions: {}", channel, count);
    }

    @Override
    public void punsubscribed(String pattern, long count) {
    }
}