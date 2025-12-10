package com.lgcns.bebee.chat.infrastructure.redis;

import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.infrastructure.redis.dto.ChatMessage;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class RedisMessagePublisherTest {
    @Mock
    private RedisPubSubAsyncCommands<String, String> asyncCommands;

    @InjectMocks
    private RedisMessagePublisher redisPublisher;

    @Captor
    private ArgumentCaptor<String> channelCaptor;

    @Captor
    private ArgumentCaptor<String> messageCaptor;

    @Test
    void publishToChatroom_성공(){
        // given
        Long chatroomId = 123L;
        ChatMessage message = ChatMessage.builder()
                .id(1L)
                .chatroomId(chatroomId)
                .senderId(1L)
                .textContent("Hello")
                .type(Chat.ChatType.TEXT)
                .createdAt(LocalDateTime.now())
                .build();

        RedisFuture<Long> longFuture = mock(RedisFuture.class);
        RedisFuture<Void> voidFuture = mock(RedisFuture.class);

        when(asyncCommands.publish(anyString(), anyString())).thenReturn(longFuture);
        when(longFuture.thenAccept(any())).thenReturn(voidFuture);

        // when
        redisPublisher.publishToChatroom(chatroomId, message);

        // then
        verify(asyncCommands).publish(channelCaptor.capture(), messageCaptor.capture());
        assertThat(channelCaptor.getValue()).isEqualTo("chatroom:123");
        assertThat(messageCaptor.getValue()).contains("\"textContent\":\"Hello\"");
    }

    @Test
    void publishToChatroom_JSON직렬화실패(){
        // json 변환 실패 시 Redis publish 함수 실행되면 안된다.
        verify(asyncCommands, never()).publish(channelCaptor.capture(), messageCaptor.capture());
    }
}
