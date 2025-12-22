package com.lgcns.bebee.chat.infrastructure.redis;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Redis 채널 구독 테스트")
public class RedisChannelSubscriberTest {
    @Mock
    private RedisPubSubAsyncCommands<String, String> asyncCommands;

    @InjectMocks
    private RedisChannelSubscriber redisChannelSubscriber;

    @Test
    void subscribeToMember_성공(){
        // given
        Long memberId = 123L;

        RedisFuture<Void> future = mock(RedisFuture.class);
        when(asyncCommands.subscribe(anyString())).thenReturn(future);
        when(future.thenAccept(any())).thenReturn(future);
        when(future.exceptionally(any())).thenReturn(future);

        // when
        redisChannelSubscriber.subscribeToMember(memberId);

        // then
        verify(asyncCommands).subscribe(any());
    }
}
