package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.application.client.MessagePublisher;
import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.repository.ChatRepository;
import com.lgcns.bebee.chat.domain.service.ChatroomManagement;
import com.lgcns.bebee.chat.domain.service.MemberManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("채팅 메시지 발송 유스케이스 테스트")
class SendChatMessageUseCaseTest {

    @Mock
    private MessagePublisher messagePublisher;
    @Mock
    private ChatroomManagement chatroomManagement;
    @Mock
    private MemberManagement memberManagement;
    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private SendChatMessageUseCase sendChatMessageUseCase;

    @Mock
    private MemberSync mockSender;
    @Mock
    private MemberSync mockReceiver;
    @Mock
    private Chatroom mockChatroom;
    @Mock
    private Chat mockChat;

    @BeforeEach
    void setUp() {
        lenient().when(mockSender.getId()).thenReturn(1L);
        lenient().when(mockReceiver.getId()).thenReturn(2L);
        lenient().when(mockChatroom.getId()).thenReturn(100L);
        lenient().when(mockChatroom.getMember1()).thenReturn(mockSender);
        lenient().when(mockChatroom.getMember2()).thenReturn(mockReceiver);
        lenient().when(mockChat.getId()).thenReturn(1000L);
    }

    @DisplayName("성공적으로 채팅 메시지를 발송하고 마지막 메시지를 업데이트한다")
    @Test
    void success_SendMessageAndUpdateLastMessage() {
        // Given
        Long chatroomId = 100L;
        Long senderId = 1L;
        Long receiverId = 2L;
        String textContent = "Hello, world!";
        String chatType = "TEXT";
        LocalDateTime now = LocalDateTime.now();

        SendChatMessageUseCase.Param param = new SendChatMessageUseCase.Param(
                chatroomId, senderId, receiverId, textContent, chatType,
                Collections.<String>emptyList(), // attachments
                (Long) null, (String) null, (String) null, // agreementId, matchType, startDate
                (String) null, // endDate
                Collections.<String>emptyList(), // scheduleDays
                Collections.<String>emptyList(), // scheduleStartTimes
                Collections.<String>emptyList(), // scheduleEndTimes
                (String) null, (Integer) null, (Integer) null, (String) null, now // location, unitPoints, totalPoints, matchStatus, createdAt
        );

        when(memberManagement.getExistingMember(senderId)).thenReturn(mockSender);
        when(memberManagement.getExistingMember(receiverId)).thenReturn(mockReceiver);
        when(chatroomManagement.getExistingChatroom(chatroomId)).thenReturn(mockChatroom);

        ArgumentCaptor<Chat> chatCaptor = ArgumentCaptor.forClass(Chat.class);
        when(chatRepository.save(chatCaptor.capture())).thenReturn(mockChat);

        // When
        sendChatMessageUseCase.execute(param);

        // Then
        verify(memberManagement).getExistingMember(senderId);
        verify(memberManagement).getExistingMember(receiverId);

        verify(chatroomManagement).getExistingChatroom(chatroomId);
        verify(chatRepository).save(any(Chat.class));

        Chat savedChat = chatCaptor.getValue();
        assertThat(savedChat.getChatroomId()).isEqualTo(mockChatroom.getId());
        assertThat(savedChat.getSenderId()).isEqualTo(senderId);
        assertThat(savedChat.getTextContent()).isEqualTo(textContent);
        assertThat(savedChat.getType().name()).isEqualTo(chatType);

        verify(messagePublisher).publishToMember(eq(senderId), eq(receiverId), any(Chat.class));
        verify(chatroomManagement).updateLastMessage(eq(mockChatroom), eq(savedChat));
    }
}