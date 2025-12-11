package com.lgcns.bebee.chat.infrastructure.redis;

import com.lgcns.bebee.chat.application.client.ChannelSubscriber;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.lgcns.bebee.chat.infrastructure.redis.RedisUtils.getMemberChannel;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisChannelSubscriber implements ChannelSubscriber {
    private final RedisPubSubAsyncCommands<String, String> asyncCommands;

    /**
     * 특정 회원 채널을 구독합니다.
     * 1:1 채팅에서 회원은 자신의 채널을 구독합니다.
     *
     * @param memberId 회원 ID
     */
    public void subscribeToMember(Long memberId) {
        String channel = getMemberChannel(memberId);
        asyncCommands.subscribe(channel)
                .thenAccept(result ->
                    log.info("Subscribed to channel: {}", channel))
                .exceptionally(throwable -> {
                    log.error("Failed to subscribe to channel: {}", channel, throwable);
                    return null;
                });
    }

    /**
     * 특정 회원 채널 구독을 해제합니다.
     *
     * @param memberId 회원 ID
     */
    public void unsubscribeFromMember(Long memberId) {
        String channel = getMemberChannel(memberId);
        asyncCommands.unsubscribe(channel)
                .thenAccept(result ->
                    log.info("Unsubscribed from channel: {}", channel))
                .exceptionally(throwable -> {
                    log.error("Failed to unsubscribe from channel: {}", channel, throwable);
                    return null;
                });
    }
}