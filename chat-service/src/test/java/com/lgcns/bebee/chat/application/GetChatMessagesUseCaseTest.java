package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.repository.ChatRepository;
import com.lgcns.bebee.chat.domain.service.ChatroomManagement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Limit;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("채팅 메시지 조회 유스케이스 테스트")
class GetChatMessagesUseCaseTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatroomManagement chatroomManagement;

    @InjectMocks
    private GetChatMessagesUseCase useCase;

    private Chatroom createMockChatroom(Long chatroomId) {
        Chatroom chatroom = mock(Chatroom.class);
        when(chatroom.getId()).thenReturn(chatroomId);
        return chatroom;
    }

    private List<Chat> createMockChats(int count) {
        List<Chat> chats = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Chat chat = mock(Chat.class);
            lenient().when(chat.getId()).thenReturn(1000L - i);
            chats.add(chat);
        }
        return chats;
    }

    @Test
    @DisplayName("첫 조회 시 lastChatId가 null이면 Long.MAX_VALUE를 사용하여 최신 메시지부터 조회한다")
    void success_FirstLoad_WithNullLastChatId() {
        // given
        Long chatroomId = 1L;
        Integer count = 20;
        Chatroom mockChatroom = createMockChatroom(chatroomId);
        List<Chat> mockChats = createMockChats(20);

        when(chatroomManagement.getExistingChatroom(chatroomId)).thenReturn(mockChatroom);
        when(chatRepository.findByChatroomIdAndIdLessThanOrderByIdDesc(
                eq(chatroomId), eq(Long.MAX_VALUE), any(Limit.class)))
                .thenReturn(mockChats);

        GetChatMessagesUseCase.Param param = new GetChatMessagesUseCase.Param(chatroomId, null, count);

        // when
        GetChatMessagesUseCase.Result result = useCase.execute(param);

        // then
        assertThat(result.getMessages()).hasSize(20);
        assertThat(result.isHasNext()).isFalse();
        assertThat(result.getNextChatId()).isNull();
    }

    @Test
    @DisplayName("lastChatId가 있으면 해당 ID 이전의 메시지를 조회한다")
    void success_WithLastChatId() {
        // given
        Long chatroomId = 1L;
        Long lastChatId = 500L;
        Integer count = 20;
        Chatroom mockChatroom = createMockChatroom(chatroomId);
        List<Chat> mockChats = createMockChats(20);

        when(chatroomManagement.getExistingChatroom(chatroomId)).thenReturn(mockChatroom);
        when(chatRepository.findByChatroomIdAndIdLessThanOrderByIdDesc(
                eq(chatroomId), eq(lastChatId), any(Limit.class)))
                .thenReturn(mockChats);

        GetChatMessagesUseCase.Param param = new GetChatMessagesUseCase.Param(chatroomId, lastChatId, count);

        // when
        GetChatMessagesUseCase.Result result = useCase.execute(param);

        // then
        assertThat(result.getMessages()).hasSize(20);
        assertThat(result.isHasNext()).isFalse();
        assertThat(result.getNextChatId()).isNull();
    }

    @Test
    @DisplayName("조회된 메시지가 count+1개면 hasNext가 true이고 nextChatId를 반환한다")
    void success_HasNext_True() {
        // given
        Long chatroomId = 1L;
        Integer count = 20;
        Chatroom mockChatroom = createMockChatroom(chatroomId);
        List<Chat> mockChats = createMockChats(21); // count + 1개

        when(chatroomManagement.getExistingChatroom(chatroomId)).thenReturn(mockChatroom);
        when(chatRepository.findByChatroomIdAndIdLessThanOrderByIdDesc(
                eq(chatroomId), eq(Long.MAX_VALUE), eq(Limit.of(21))))
                .thenReturn(mockChats);

        GetChatMessagesUseCase.Param param = new GetChatMessagesUseCase.Param(chatroomId, null, count);

        // when
        GetChatMessagesUseCase.Result result = useCase.execute(param);

        // then
        assertThat(result.getMessages()).hasSize(20); // count개만 반환
        assertThat(result.isHasNext()).isTrue();
        assertThat(result.getNextChatId()).isEqualTo(980L); // 21번째 메시지의 ID (1000 - 20)
    }

    @Test
    @DisplayName("조회된 메시지가 count개 이하면 hasNext가 false이고 nextChatId가 null이다")
    void success_HasNext_False() {
        // given
        Long chatroomId = 1L;
        Integer count = 20;
        Chatroom mockChatroom = createMockChatroom(chatroomId);
        List<Chat> mockChats = createMockChats(15); // count보다 적음

        when(chatroomManagement.getExistingChatroom(chatroomId)).thenReturn(mockChatroom);
        when(chatRepository.findByChatroomIdAndIdLessThanOrderByIdDesc(
                eq(chatroomId), eq(Long.MAX_VALUE), any(Limit.class)))
                .thenReturn(mockChats);

        GetChatMessagesUseCase.Param param = new GetChatMessagesUseCase.Param(chatroomId, null, count);

        // when
        GetChatMessagesUseCase.Result result = useCase.execute(param);

        // then
        assertThat(result.getMessages()).hasSize(15);
        assertThat(result.isHasNext()).isFalse();
        assertThat(result.getNextChatId()).isNull();
    }
}