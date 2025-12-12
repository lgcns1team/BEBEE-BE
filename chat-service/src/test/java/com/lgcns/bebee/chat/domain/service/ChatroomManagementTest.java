package com.lgcns.bebee.chat.domain.service;

import com.lgcns.bebee.chat.core.exception.ChatException;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.repository.ChatroomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatroomManagement 단위 테스트")
class ChatroomManagementTest {

    @Mock
    private ChatroomRepository chatroomRepository;

    @InjectMocks
    private ChatroomManagement chatroomManagement;

    private Chatroom chatroom;

    @BeforeEach
    void setUp() {
        chatroom = Chatroom.builder()
                .member1Id(1L)
                .member2Id(2L)
                .build();
    }

    @Nested
    @DisplayName("getExistingChatroom 테스트")
    class GetExistingChatroomTest {

        @Test
        @DisplayName("채팅방이 존재하면 성공적으로 조회한다")
        void success() {
            // given
            Long chatroomId = 1L;
            when(chatroomRepository.findById(chatroomId)).thenReturn(Optional.of(chatroom));

            // when
            Chatroom result = chatroomManagement.getExistingChatroom(chatroomId);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(chatroom);
            verify(chatroomRepository, times(1)).findById(chatroomId);
        }

        @Test
        @DisplayName("채팅방이 존재하지 않으면 예외를 발생시킨다")
        void throwsException_WhenChatroomNotFound() {
            // given
            Long chatroomId = 999L;
            when(chatroomRepository.findById(chatroomId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> chatroomManagement.getExistingChatroom(chatroomId))
                    .isInstanceOf(ChatException.class);
            verify(chatroomRepository, times(1)).findById(chatroomId);
        }
    }

    @Nested
    @DisplayName("openChatroom 테스트")
    class OpenChatroomTest {

        @Test
        @DisplayName("chatroomId가 주어지고 채팅방이 존재하면 해당 채팅방을 반환한다")
        void success_WithChatroomId() {
            // given
            Long chatroomId = 1L;
            Long senderId = 100L;
            Long receiverId = 200L;
            when(chatroomRepository.findById(chatroomId)).thenReturn(Optional.of(chatroom));

            // when
            Chatroom result = chatroomManagement.openChatroom(chatroomId, senderId, receiverId);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(chatroom);
            verify(chatroomRepository, times(1)).findById(chatroomId);
            verify(chatroomRepository, never()).findChatroom(any(), any());
            verify(chatroomRepository, never()).save(any());
        }

        @Test
        @DisplayName("chatroomId가 null이고 기존 채팅방이 있으면 기존 채팅방을 반환한다")
        void success_WithoutChatroomId_ExistingChatroom() {
            // given
            Long senderId = 1L;
            Long receiverId = 2L;
            Long member1 = Math.min(senderId, receiverId);
            Long member2 = Math.max(senderId, receiverId);
            when(chatroomRepository.findChatroom(member1, member2)).thenReturn(Optional.of(chatroom));

            // when
            Chatroom result = chatroomManagement.openChatroom(null, senderId, receiverId);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(chatroom);
            verify(chatroomRepository, times(1)).findChatroom(member1, member2);
            verify(chatroomRepository, never()).save(any());
        }

        @Test
        @DisplayName("chatroomId가 null이고 기존 채팅방이 없으면 새 채팅방을 생성한다")
        void success_WithoutChatroomId_NewChatroom() {
            // given
            Long senderId = 1L;
            Long receiverId = 2L;
            Long member1 = Math.min(senderId, receiverId);
            Long member2 = Math.max(senderId, receiverId);
            when(chatroomRepository.findChatroom(member1, member2)).thenReturn(Optional.empty());

            // when
            Chatroom result = chatroomManagement.openChatroom(null, senderId, receiverId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getMember1Id()).isEqualTo(member1);
            assertThat(result.getMember2Id()).isEqualTo(member2);
            verify(chatroomRepository, times(1)).findChatroom(member1, member2);
        }

        @Test
        @DisplayName("senderId와 receiverId의 순서와 관계없이 일관된 member1, member2를 유지한다")
        void maintainsConsistentMemberOrder() {
            // given
            Long senderId = 2L;
            Long receiverId = 1L;
            Long member1 = 1L; // 작은 값
            Long member2 = 2L; // 큰 값
            when(chatroomRepository.findChatroom(member1, member2)).thenReturn(Optional.empty());

            // when
            Chatroom result = chatroomManagement.openChatroom(null, senderId, receiverId);

            // then
            assertThat(result.getMember1Id()).isEqualTo(member1);
            assertThat(result.getMember2Id()).isEqualTo(member2);
            verify(chatroomRepository, times(1)).findChatroom(member1, member2);
        }
    }
}