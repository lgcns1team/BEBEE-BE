package com.lgcns.bebee.chat.infrastructure.redis;

import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.lgcns.bebee.chat.infrastructure.redis.RedisUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisChannelSubscriber {
    private final RedisPubSubAsyncCommands<String, String> asyncCommands;

    /**
     * 특정 채팅방 채널을 구독합니다.
     *
     * @param chatroomId 채팅방 ID
     */
    public void subscribeToChatroom(Long chatroomId) {
        String channel = getChatroomChannel(chatroomId);
        asyncCommands.subscribe(channel)
                .thenAccept(result ->
                    log.info("Subscribed to channel: {}", channel))
                .exceptionally(throwable -> {
                    log.error("Failed to subscribe to channel: {}", channel, throwable);
                    return null;
                });
    }

    /**
     * 특정 채팅방 채널 구독을 해제합니다.
     *
     * @param chatroomId 채팅방 ID
     */
    public void unsubscribeFromChatroom(Long chatroomId) {
        String channel = getChatroomChannel(chatroomId);
        asyncCommands.unsubscribe(channel)
                .thenAccept(result ->
                    log.info("Unsubscribed from channel: {}", channel))
                .exceptionally(throwable -> {
                    log.error("Failed to unsubscribe from channel: {}", channel, throwable);
                    return null;
                });
    }
}