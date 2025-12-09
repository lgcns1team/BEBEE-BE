package com.lgcns.bebee.chat.infrastructure.redis;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class RedisChannelSubscriberTest {
    @Mock
    private RedisPubSubAsyncCommands<String, String> asyncCommands;

    @InjectMocks
    private RedisChannelSubscriber redisSubscriptionManager;

    @Test
    void subscribeToChatroom_성공(){
        // given
        Long chatroomId = 123L;

        RedisFuture<Void> future = mock(RedisFuture.class);
        when(asyncCommands.subscribe(anyString())).thenReturn(future);
        when(future.thenAccept(any())).thenReturn(future);
        when(future.exceptionally(any())).thenReturn(future);

        // when
        redisSubscriptionManager.subscribeToChatroom(chatroomId);

        // then
        verify(asyncCommands).subscribe(any());
    }
}
