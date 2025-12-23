package com.lgcns.bebee.chat.domain.service;

import com.lgcns.bebee.chat.core.exception.ChatException;
import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.HelpCategorySync;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.repository.ChatroomRepository;
import com.lgcns.bebee.chat.domain.repository.HelpCategorySyncRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatroomManagement 단위 테스트")
class ChatroomManagementTest {

    @Mock
    private ChatroomRepository chatroomRepository;
    @Mock
    private HelpCategorySyncRepository helpCategorySyncRepository;

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
    @DisplayName("findChatroomWithMembers(Long) 테스트")
    class FindChatroomWithMembersByIdTest {

        @Test
        @DisplayName("채팅방 ID로 멤버 정보와 함께 성공적으로 조회한다")
        void success() {
            // given
            Long chatroomId = 1L;
            Chatroom chatroom = mock(Chatroom.class);
            when(chatroomRepository.findChatroomWithMembers(chatroomId))
                    .thenReturn(Optional.of(chatroom));

            // when
            Chatroom result = chatroomManagement.findChatroomWithMembers(chatroomId);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(chatroom);
            verify(chatroomRepository, times(1)).findChatroomWithMembers(chatroomId);
        }

        @Test
        @DisplayName("채팅방이 존재하지 않으면 예외를 발생시킨다")
        void throwsException_WhenChatroomNotFound() {
            // given
            Long chatroomId = 999L;
            when(chatroomRepository.findChatroomWithMembers(chatroomId))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> chatroomManagement.findChatroomWithMembers(chatroomId))
                    .isInstanceOf(ChatException.class);
            verify(chatroomRepository, times(1)).findChatroomWithMembers(chatroomId);
        }
    }

    @Nested
    @DisplayName("findChatroomWithMembers(MemberSync, MemberSync) 테스트")
    class FindChatroomWithMembersByMembersTest {

        @Test
        @DisplayName("기존 채팅방이 존재하면 해당 채팅방을 반환한다")
        void success_ExistingChatroom() {
            // given
            MemberSync currentMember = mock(MemberSync.class);
            MemberSync otherMember = mock(MemberSync.class);
            Chatroom chatroom = mock(Chatroom.class);

            when(chatroomRepository.findChatroomWithMembers(currentMember, otherMember))
                    .thenReturn(Optional.of(chatroom));

            // when
            Chatroom result = chatroomManagement.findChatroomWithMembers(currentMember, otherMember);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(chatroom);
            verify(chatroomRepository, times(1)).findChatroomWithMembers(currentMember, otherMember);
            verify(chatroomRepository, never()).save(any(MemberSync.class), any(MemberSync.class));
        }

        @Test
        @DisplayName("기존 채팅방이 없으면 새로운 채팅방을 생성한다")
        void success_CreateNewChatroom() {
            // given
            MemberSync currentMember = mock(MemberSync.class);
            MemberSync otherMember = mock(MemberSync.class);
            Chatroom newChatroom = mock(Chatroom.class);

            when(chatroomRepository.findChatroomWithMembers(currentMember, otherMember))
                    .thenReturn(Optional.empty());
            when(chatroomRepository.save(currentMember, otherMember))
                    .thenReturn(newChatroom);

            // when
            Chatroom result = chatroomManagement.findChatroomWithMembers(currentMember, otherMember);

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(newChatroom);
            verify(chatroomRepository, times(1)).findChatroomWithMembers(currentMember, otherMember);
            verify(chatroomRepository, times(1)).save(currentMember, otherMember);
        }
    }

    @Nested
    @DisplayName("linkPost 테스트")
    class LinkPostTest {

        @Test
        @DisplayName("게시글 ID, 제목, 도움 카테고리와 함께 채팅방을 연결한다")
        void success_WithHelpCategories() {
            // given
            Chatroom chatroom = mock(Chatroom.class);
            Long postId = 100L;
            String postTitle = "도와주세요";
            List<Long> helpCategoryIds = Arrays.asList(1L, 2L);

            HelpCategorySync category1 = mock(HelpCategorySync.class);
            HelpCategorySync category2 = mock(HelpCategorySync.class);
            List<HelpCategorySync> helpCategories = Arrays.asList(category1, category2);

            when(helpCategorySyncRepository.findByIds(helpCategoryIds))
                    .thenReturn(helpCategories);

            // when
            chatroomManagement.linkPost(chatroom, postId, postTitle, helpCategoryIds);

            // then
            verify(helpCategorySyncRepository, times(1)).findByIds(helpCategoryIds);
            verify(chatroom, times(1)).linkPost(postId, postTitle, helpCategories);
        }

        @Test
        @DisplayName("도움 카테고리 ID가 null이면 null로 연결한다")
        void success_WithNullHelpCategoryIds() {
            // given
            Chatroom chatroom = mock(Chatroom.class);
            Long postId = 100L;
            String postTitle = "도와주세요";
            List<Long> helpCategoryIds = null;

            // when
            chatroomManagement.linkPost(chatroom, postId, postTitle, helpCategoryIds);

            // then
            verify(helpCategorySyncRepository, never()).findByIds(any());
            verify(chatroom, times(1)).linkPost(postId, postTitle, null);
        }

        @Test
        @DisplayName("도움 카테고리 ID가 빈 리스트면 null로 연결한다")
        void success_WithEmptyHelpCategoryIds() {
            // given
            Chatroom chatroom = mock(Chatroom.class);
            Long postId = 100L;
            String postTitle = "도와주세요";
            List<Long> helpCategoryIds = Collections.emptyList();

            // when
            chatroomManagement.linkPost(chatroom, postId, postTitle, helpCategoryIds);

            // then
            verify(helpCategorySyncRepository, never()).findByIds(any());
            verify(chatroom, times(1)).linkPost(postId, postTitle, null);
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