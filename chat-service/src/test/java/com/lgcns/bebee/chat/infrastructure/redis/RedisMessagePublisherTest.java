package com.lgcns.bebee.chat.infrastructure.redis;

import com.lgcns.bebee.chat.domain.entity.Chat;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Redis 메시지 발행 테스트")
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
                null, // agreementId,
                null,
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

        // 채널 검증
        assertThat(capturedChannels).hasSize(2);
        assertThat(capturedChannels.get(0)).isEqualTo("member:2"); // 수신자 채널
        assertThat(capturedChannels.get(1)).isEqualTo("member:1"); // 발신자 채널

        // 메시지 내용 검증
        String messageForReceiver = capturedMessages.get(0);
        String messageForSender = capturedMessages.get(1);

        // 수신자에게 보낸 메시지 - receiverId는 발신자(상대방)
        assertThat(messageForReceiver).contains("\"textContent\":\"Hello\"");
        assertThat(messageForReceiver).contains("\"senderId\":1");
        assertThat(messageForReceiver).contains("\"receiverId\":2"); // 상대방 = senderId

        // 발신자에게 보낸 메시지 - receiverId는 수신자(상대방)
        assertThat(messageForSender).contains("\"textContent\":\"Hello\"");
        assertThat(messageForSender).contains("\"senderId\":1");
        assertThat(messageForSender).contains("\"receiverId\":1"); // 상대방 = receiverId
    }

    @Test
    void publishToMember_JSON직렬화실패(){
        // json 변환 실패 시 Redis publish 함수 실행되면 안된다.
        verify(asyncCommands, never()).publish(channelCaptor.capture(), messageCaptor.capture());
    }
}
