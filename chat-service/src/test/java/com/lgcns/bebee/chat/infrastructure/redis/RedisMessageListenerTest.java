package com.lgcns.bebee.chat.infrastructure.redis;

import com.lgcns.bebee.chat.infrastructure.dto.ChatMessageDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class RedisMessageListenerTest {
    @Mock
    private SimpMessageSendingOperations messagingTemplate;

    @InjectMocks
    private RedisMessageListener redisSubscriber;

    @Captor
    private ArgumentCaptor<String> channelCaptor;

    @Captor
    private ArgumentCaptor<ChatMessageDTO> messageCaptor;

    @Test
    void message_성공(){
        // given
        String channel = "member:123";
        String messageJson = """
                {
                    "id": 1,
                    "chatroomId": 123,
                    "senderId": 1,
                    "textContent": "Hello",
                    "type": "TEXT",
                    "createdAt": "2025-01-01T10:00:00"
                }
                """;
        // when
        redisSubscriber.message(channel, messageJson);

        // then
        verify(messagingTemplate).convertAndSend(channelCaptor.capture(), messageCaptor.capture());
        assertThat(channelCaptor.getValue()).endsWith(channel);
        assertThat(messageCaptor.getValue().textContent()).isEqualTo("Hello");
    }

    @Test
    void message_잘못된JSON(){
        // given
        String channel = "member:123";
        String invalidJson = "{ invalid json }";

        // when
        redisSubscriber.message(channel, invalidJson);

        // then
        verify(messagingTemplate, never()).convertAndSend(anyString(), any(ChatMessageDTO.class));
    }
}
