package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.service.ChatroomManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("채팅방 조회 유스케이스 테스트")
class GetChatroomUseCaseTest {

    @Mock
    private ChatroomManagement chatroomManagement;

    @InjectMocks
    private GetChatroomUseCase getChatroomUseCase;

    @Mock
    private Chatroom mockChatroom;
    @Mock
    private MemberSync member1;
    @Mock
    private MemberSync member2;

    @BeforeEach
    void setUp() {
        // Setup Member1
        lenient().when(member1.getId()).thenReturn(1L);
        lenient().when(member1.getNickname()).thenReturn("nickname1");
        lenient().when(member1.getProfileImageUrl()).thenReturn("url1");
        lenient().when(member1.getSweetness()).thenReturn(BigDecimal.ZERO);

        // Setup Member2
        lenient().when(member2.getId()).thenReturn(2L);
        lenient().when(member2.getNickname()).thenReturn("nickname2");
        lenient().when(member2.getProfileImageUrl()).thenReturn("url2");
        lenient().when(member2.getSweetness()).thenReturn(BigDecimal.ONE);

        // Setup Chatroom
        lenient().when(mockChatroom.getId()).thenReturn(100L);
        lenient().when(mockChatroom.getMember1()).thenReturn(member1);
        lenient().when(mockChatroom.getMember2()).thenReturn(member2);
    }

    @DisplayName("채팅방 ID가 주어진 경우 ID와 회원 정보로 채팅방을 조회한다")
    @Test
    void success_WithChatroomId() {
        // Given
        Long chatroomId = 100L;
        Long currentMemberId = 1L;
        Long otherMemberId = null;
        GetChatroomUseCase.Param param = new GetChatroomUseCase.Param(chatroomId, currentMemberId, otherMemberId);

        when(chatroomManagement.findChatroom(chatroomId, currentMemberId, otherMemberId))
                .thenReturn(mockChatroom);

        // When
        GetChatroomUseCase.Result result = getChatroomUseCase.execute(param);

        // Then
        verify(chatroomManagement).findChatroom(eq(chatroomId), eq(currentMemberId), eq(otherMemberId));
        assertThat(result.getChatroomId()).isEqualTo(100L);
        assertThat(result.getOtherNickname()).isEqualTo("nickname2");
    }

    @DisplayName("채팅방 ID가 없는 경우 회원 정보로 채팅방을 조회한다")
    @Test
    void success_WithoutChatroomId() {
        // Given
        Long chatroomId = null;
        Long currentMemberId = 1L;
        Long otherMemberId = 2L;
        GetChatroomUseCase.Param param = new GetChatroomUseCase.Param(chatroomId, currentMemberId, otherMemberId);

        when(chatroomManagement.findChatroom(null, currentMemberId, otherMemberId))
                .thenReturn(mockChatroom);

        // When
        GetChatroomUseCase.Result result = getChatroomUseCase.execute(param);

        // Then
        verify(chatroomManagement).findChatroom(isNull(), eq(currentMemberId), eq(otherMemberId));
        assertThat(result.getChatroomId()).isEqualTo(100L); // Mock returns 100L
        assertThat(result.getMyId()).isEqualTo(currentMemberId);
        assertThat(result.getOtherId()).isEqualTo(otherMemberId);
        assertThat(result.getOtherNickname()).isEqualTo("nickname2");
    }
}
