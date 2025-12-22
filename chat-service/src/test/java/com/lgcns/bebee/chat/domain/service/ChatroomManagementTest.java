package com.lgcns.bebee.chat.domain.service;

import com.lgcns.bebee.chat.core.exception.ChatException;
import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.repository.ChatroomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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

    @Nested
    @DisplayName("getExistingChatroom 테스트")
    class GetExistingChatroomTest {

        @Test
        @DisplayName("채팅방이 존재하면 성공적으로 조회한다")
        void success() {
            // given
            Long chatroomId = 1L;
            Chatroom chatroom = mock(Chatroom.class);
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
    @DisplayName("findChatroom 테스트")
    class FindChatroomTest {

        @Test
        @DisplayName("chatroomId와 memberId들로 채팅방을 성공적으로 조회한다")
        void success() {
            // given
            Long chatroomId = 1L;
            Long currentMemberId = 10L;
            Long otherMemberId = 20L;
            Chatroom chatroom = mock(Chatroom.class);
            when(chatroomRepository.findChatroomWithMembers(chatroomId, currentMemberId, otherMemberId))
                    .thenReturn(Optional.of(chatroom));

            // when
            Chatroom result = chatroomManagement.findChatroom(chatroomId, currentMemberId, otherMemberId);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(chatroom);
            verify(chatroomRepository, times(1)).findChatroomWithMembers(chatroomId, currentMemberId, otherMemberId);
        }

        @Test
        @DisplayName("채팅방이 존재하지 않으면 예외를 발생시킨다")
        void throwsException_WhenChatroomNotFound() {
            // given
            Long chatroomId = 999L;
            Long currentMemberId = 10L;
            Long otherMemberId = 20L;
            when(chatroomRepository.findChatroomWithMembers(chatroomId, currentMemberId, otherMemberId))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> chatroomManagement.findChatroom(chatroomId, currentMemberId, otherMemberId))
                    .isInstanceOf(ChatException.class);
            verify(chatroomRepository, times(1)).findChatroomWithMembers(chatroomId, currentMemberId, otherMemberId);
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
            MemberSync sender = mock(MemberSync.class);
            MemberSync receiver = mock(MemberSync.class);
            Chatroom chatroom = mock(Chatroom.class);
            when(chatroomRepository.findById(chatroomId)).thenReturn(Optional.of(chatroom));

            // when
            Chatroom result = chatroomManagement.openChatroom(chatroomId, sender, receiver);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(chatroom);
            verify(chatroomRepository, times(1)).findById(chatroomId);
            verify(chatroomRepository, never()).findChatroom(any(MemberSync.class), any(MemberSync.class));
            verify(chatroomRepository, never()).save(any(MemberSync.class), any(MemberSync.class));
        }

        @Test
        @DisplayName("chatroomId가 null이고 기존 채팅방이 있으면 기존 채팅방을 반환한다")
        void success_WithoutChatroomId_ExistingChatroom() {
            // given
            MemberSync sender = mock(MemberSync.class);
            MemberSync receiver = mock(MemberSync.class);

            Chatroom chatroom = mock(Chatroom.class);
            when(chatroomRepository.findChatroom(sender, receiver)).thenReturn(Optional.of(chatroom));

            // when
            Chatroom result = chatroomManagement.openChatroom(null, sender, receiver);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(chatroom);
            verify(chatroomRepository, times(1)).findChatroom(sender, receiver);
            verify(chatroomRepository, never()).save(any(MemberSync.class), any(MemberSync.class));
        }

        @Test
        @DisplayName("chatroomId가 null이고 기존 채팅방이 없으면 새 채팅방을 생성한다")
        void success_WithoutChatroomId_NewChatroom() {
            // given
            MemberSync sender = mock(MemberSync.class);
            MemberSync receiver = mock(MemberSync.class);
            when(sender.getId()).thenReturn(1L);
            when(receiver.getId()).thenReturn(2L);

            Chatroom newChatroom = mock(Chatroom.class);
            when(newChatroom.getMember1()).thenReturn(sender);
            when(newChatroom.getMember2()).thenReturn(receiver);

            when(chatroomRepository.findChatroom(sender, receiver)).thenReturn(Optional.empty());
            when(chatroomRepository.save(sender, receiver)).thenReturn(newChatroom);

            // when
            Chatroom result = chatroomManagement.openChatroom(null, sender, receiver);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getMember1().getId()).isEqualTo(1L);
            assertThat(result.getMember2().getId()).isEqualTo(2L);
            verify(chatroomRepository, times(1)).findChatroom(sender, receiver);
            verify(chatroomRepository, times(1)).save(sender, receiver);
        }
    }

    @Nested
    @DisplayName("updateLastMessage 테스트")
    class UpdateLastMessageTest {

        @Test
        @DisplayName("TEXT 타입 - 31자 미만의 텍스트는 그대로 저장된다")
        void success_TextType_LessThan31Characters() {
            // given
            Chatroom chatroom = mock(Chatroom.class);
            String textContent = "안녕하세요, 반갑습니다!";
            Chat chat = Chat.create(
                    1L, 100L, textContent, "TEXT", null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, LocalDateTime.now()
            );

            // when
            chatroomManagement.updateLastMessage(chatroom, chat);

            // then
            verify(chatroom, times(1)).updateLastMessage(textContent);
        }

        @Test
        @DisplayName("TEXT 타입 - 정확히 31자인 텍스트는 그대로 저장된다")
        void success_TextType_Exactly31Characters() {
            // given
            Chatroom chatroom = mock(Chatroom.class);
            String textContent = "1234567890123456789012345678901"; // 31자
            Chat chat = Chat.create(
                    1L, 100L, textContent, "TEXT", null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, LocalDateTime.now()
            );

            // when
            chatroomManagement.updateLastMessage(chatroom, chat);

            // then
            verify(chatroom, times(1)).updateLastMessage(textContent);
        }

        @Test
        @DisplayName("TEXT 타입 - 31자 초과 텍스트는 31자로 잘려서 저장된다")
        void success_TextType_MoreThan31Characters() {
            // given
            Chatroom chatroom = mock(Chatroom.class);
            String textContent = "12345678901234567890123456789012345"; // 35자
            String expected = "1234567890123456789012345678901"; // 앞 31자
            Chat chat = Chat.create(
                    1L, 100L, textContent, "TEXT", null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, LocalDateTime.now()
            );

            // when
            chatroomManagement.updateLastMessage(chatroom, chat);

            // then
            verify(chatroom, times(1)).updateLastMessage(expected);
        }

        @Test
        @DisplayName("TEXT 타입 - null 텍스트는 빈 문자열로 저장된다")
        void success_TextType_NullContent() {
            // given
            Chatroom chatroom = mock(Chatroom.class);
            Chat chat = Chat.create(
                    1L, 100L, null, "TEXT", null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, LocalDateTime.now()
            );

            // when
            chatroomManagement.updateLastMessage(chatroom, chat);

            // then
            verify(chatroom, times(1)).updateLastMessage("");
        }

        @Test
        @DisplayName("TEXT 타입 - 빈 문자열은 빈 문자열로 저장된다")
        void success_TextType_EmptyContent() {
            // given
            Chatroom chatroom = mock(Chatroom.class);
            Chat chat = Chat.create(
                    1L, 100L, "", "TEXT", null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, LocalDateTime.now()
            );

            // when
            chatroomManagement.updateLastMessage(chatroom, chat);

            // then
            verify(chatroom, times(1)).updateLastMessage("");
        }

        @Test
        @DisplayName("IMAGE 타입 - '(이미지)'로 저장된다")
        void success_ImageType() {
            // given
            Chatroom chatroom = mock(Chatroom.class);
            Chat chat = Chat.create(
                    1L, 100L, null, "IMAGE", null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, LocalDateTime.now()
            );

            // when
            chatroomManagement.updateLastMessage(chatroom, chat);

            // then
            verify(chatroom, times(1)).updateLastMessage("(이미지)");
        }

        @Test
        @DisplayName("MATCH_CONFIRMATION 타입 - '(매칭 확인서)'로 저장된다")
        void success_MatchConfirmationType() {
            // given
            Chatroom chatroom = mock(Chatroom.class);
            Chat chat = Chat.create(
                    1L, 100L, null, "MATCH_CONFIRMATION", null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, LocalDateTime.now()
            );

            // when
            chatroomManagement.updateLastMessage(chatroom, chat);

            // then
            verify(chatroom, times(1)).updateLastMessage("(매칭 확인서)");
        }

        @Test
        @DisplayName("MATCH_SUCCESS 타입 - 업데이트하지 않는다")
        void success_MatchSuccessType_DoesNotUpdate() {
            // given
            Chatroom chatroom = mock(Chatroom.class);
            Chat chat = Chat.create(
                    1L, 100L, null, "MATCH_SUCCESS", null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, LocalDateTime.now()
            );

            // when
            chatroomManagement.updateLastMessage(chatroom, chat);

            // then
            verify(chatroom, never()).updateLastMessage(any());
        }

        @Test
        @DisplayName("MATCH_FAILURE 타입 - 업데이트하지 않는다")
        void success_MatchFailureType_DoesNotUpdate() {
            // given
            Chatroom chatroom = mock(Chatroom.class);
            Chat chat = Chat.create(
                    1L, 100L, null, "MATCH_FAILURE", null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, LocalDateTime.now()
            );

            // when
            chatroomManagement.updateLastMessage(chatroom, chat);

            // then
            verify(chatroom, never()).updateLastMessage(any());
        }
    }
}