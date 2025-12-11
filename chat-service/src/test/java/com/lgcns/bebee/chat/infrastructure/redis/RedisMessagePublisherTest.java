package com.lgcns.bebee.chat.infrastructure.redis;

import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.infrastructure.dto.ChatMessageDTO;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

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
    void publishToMember_성공(){
        // given
        Long senderId = 1L;
        Long receiverId = 2L;
        Chat chat = Chat.create(
                1L, // chatroomId
                senderId, // senderId
                "Hello", // textContent
                "TEXT", // type
                null, // attachments
                null, // agreementId
                null, // startDate
                null, // endDate
                null, // scheduleDays
                null, // scheduleStartTimes
                null, // scheduleEndTimes
                null, // location
                null, // unitPoints
                null, // totalPoints
                null, // matchStatus
                LocalDateTime.now() // createdAt
        );

        RedisFuture<Long> longFuture = mock(RedisFuture.class);
        RedisFuture<Void> voidFuture = mock(RedisFuture.class);

        when(asyncCommands.publish(anyString(), anyString())).thenReturn(longFuture);
        when(longFuture.thenAccept(any())).thenReturn(voidFuture);
        when(voidFuture.exceptionally(any())).thenReturn(voidFuture);

        // when
        redisPublisher.publishToMember(senderId, receiverId, chat);

        // then
        verify(asyncCommands, times(2)).publish(channelCaptor.capture(), messageCaptor.capture());

        List<String> capturedChannels = channelCaptor.getAllValues();
        List<String> capturedMessages = messageCaptor.getAllValues();


        assertThat(capturedChannels).hasSize(2);
        assertThat(capturedChannels.get(0)).contains(String.valueOf(receiverId));
        assertThat(capturedChannels.get(1)).contains(String.valueOf(senderId));

        assertThat(channelCaptor.getValue()).isEqualTo("member:1");
        assertThat(messageCaptor.getValue()).contains("\"textContent\":\"Hello\"");
    }

    @Test
    void publishToMember_JSON직렬화실패(){
        // json 변환 실패 시 Redis publish 함수 실행되면 안된다.
        verify(asyncCommands, never()).publish(channelCaptor.capture(), messageCaptor.capture());
    }
}
