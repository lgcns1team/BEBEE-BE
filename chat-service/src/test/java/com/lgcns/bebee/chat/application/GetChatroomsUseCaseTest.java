package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.repository.ChatroomRepository;
import com.lgcns.bebee.chat.domain.service.MemberManagement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("채팅방 목록 조회 유스케이스 테스트")
class GetChatroomsUseCaseTest {

    @Mock
    private MemberManagement memberManagement;

    @Mock
    private ChatroomRepository chatroomRepository;

    @InjectMocks
    private GetChatroomsUseCase useCase;

    @Mock
    private MemberSync mockMember;

    private List<Chatroom> createMockChatrooms(int count) {
        List<Chatroom> chatrooms = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Chatroom chatroom = mock(Chatroom.class);
            lenient().when(chatroom.getId()).thenReturn(100L - i);
            chatrooms.add(chatroom);
        }
        return chatrooms;
    }

    @Test
    @DisplayName("첫 조회 시 lastChatroomId가 null이면 Long.MAX_VALUE를 사용하여 최신 채팅방부터 조회한다")
    void success_FirstLoad_WithNullLastChatroomId() {
        // given
        Long currentMemberId = 1L;
        Integer count = 10;
        List<Chatroom> mockChatrooms = createMockChatrooms(10);

        when(memberManagement.getExistingMember(currentMemberId)).thenReturn(mockMember);
        when(chatroomRepository.findChatroomsWithCursor(
                eq(mockMember), eq(Long.MAX_VALUE), eq(count + 1)))
                .thenReturn(mockChatrooms);

        GetChatroomsUseCase.Param param = new GetChatroomsUseCase.Param(currentMemberId, null, count);

        // when
        GetChatroomsUseCase.Result result = useCase.execute(param);

        // then
        assertThat(result.getChatrooms()).hasSize(10);
        assertThat(result.getHasNext()).isFalse();
        assertThat(result.getNextChatroomId()).isNull();
    }

    @Test
    @DisplayName("lastChatroomId가 있으면 해당 ID 이전의 채팅방을 조회한다")
    void success_WithLastChatroomId() {
        // given
        Long currentMemberId = 1L;
        Long lastChatroomId = 50L;
        Integer count = 10;
        List<Chatroom> mockChatrooms = createMockChatrooms(10);

        when(memberManagement.getExistingMember(currentMemberId)).thenReturn(mockMember);
        when(chatroomRepository.findChatroomsWithCursor(
                eq(mockMember), eq(lastChatroomId), eq(count + 1)))
                .thenReturn(mockChatrooms);

        GetChatroomsUseCase.Param param = new GetChatroomsUseCase.Param(currentMemberId, lastChatroomId, count);

        // when
        GetChatroomsUseCase.Result result = useCase.execute(param);

        // then
        assertThat(result.getChatrooms()).hasSize(10);
        assertThat(result.getHasNext()).isFalse();
        assertThat(result.getNextChatroomId()).isNull();
    }

    @Test
    @DisplayName("조회된 채팅방이 count+1개면 hasNext가 true이고 nextChatroomId를 반환한다")
    void success_HasNext_True() {
        // given
        Long currentMemberId = 1L;
        Integer count = 10;
        List<Chatroom> mockChatrooms = createMockChatrooms(11); // count + 1

        when(memberManagement.getExistingMember(currentMemberId)).thenReturn(mockMember);
        when(chatroomRepository.findChatroomsWithCursor(
                eq(mockMember), eq(Long.MAX_VALUE), eq(count + 1)))
                .thenReturn(mockChatrooms);

        GetChatroomsUseCase.Param param = new GetChatroomsUseCase.Param(currentMemberId, null, count);

        // when
        GetChatroomsUseCase.Result result = useCase.execute(param);

        // then
        assertThat(result.getChatrooms()).hasSize(10); // count개만 반환
        assertThat(result.getHasNext()).isTrue();
        assertThat(result.getNextChatroomId()).isEqualTo(90L); // 11번째 채팅방 ID (100 - 10)
    }

    @Test
    @DisplayName("조회된 채팅방이 count개 이하면 hasNext가 false이고 nextChatroomId가 null이다")
    void success_HasNext_False() {
        // given
        Long currentMemberId = 1L;
        Integer count = 10;
        List<Chatroom> mockChatrooms = createMockChatrooms(5); // Less than count

        when(memberManagement.getExistingMember(currentMemberId)).thenReturn(mockMember);
        when(chatroomRepository.findChatroomsWithCursor(
                eq(mockMember), eq(Long.MAX_VALUE), eq(count + 1)))
                .thenReturn(mockChatrooms);

        GetChatroomsUseCase.Param param = new GetChatroomsUseCase.Param(currentMemberId, null, count);

        // when
        GetChatroomsUseCase.Result result = useCase.execute(param);

        // then
        assertThat(result.getChatrooms()).hasSize(5);
        assertThat(result.getHasNext()).isFalse();
        assertThat(result.getNextChatroomId()).isNull();
    }
}
