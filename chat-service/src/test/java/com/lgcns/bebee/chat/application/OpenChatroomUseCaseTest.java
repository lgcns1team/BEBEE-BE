package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.ChatroomHelpCategory;
import com.lgcns.bebee.chat.domain.entity.HelpCategorySync;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.service.ChatroomManagement;
import com.lgcns.bebee.chat.domain.service.MemberManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("채팅방 오픈 유스케이스 테스트")
class OpenChatroomUseCaseTest {

    @Mock
    private ChatroomManagement chatroomManagement;
    @Mock
    private MemberManagement memberManagement;

    @InjectMocks
    private OpenChatroomUseCase openChatroomUseCase;

    @Mock
    private MemberSync mockCurrentMember;
    @Mock
    private MemberSync mockOtherMember;
    @Mock
    private Chatroom mockChatroom;
    @Mock
    private HelpCategorySync mockHelpCategory1;
    @Mock
    private HelpCategorySync mockHelpCategory2;

    @BeforeEach
    void setUp() {
        lenient().when(mockCurrentMember.getId()).thenReturn(1L);
        lenient().when(mockCurrentMember.getNickname()).thenReturn("CurrentUser");
        lenient().when(mockCurrentMember.getProfileImageUrl()).thenReturn("current.jpg");
        lenient().when(mockCurrentMember.getSweetness()).thenReturn(BigDecimal.valueOf(85.5));

        lenient().when(mockOtherMember.getId()).thenReturn(2L);
        lenient().when(mockOtherMember.getNickname()).thenReturn("OtherUser");
        lenient().when(mockOtherMember.getProfileImageUrl()).thenReturn("other.jpg");
        lenient().when(mockOtherMember.getSweetness()).thenReturn(BigDecimal.valueOf(90.0));

        lenient().when(mockChatroom.getId()).thenReturn(100L);
        lenient().when(mockChatroom.getMember1()).thenReturn(mockCurrentMember);
        lenient().when(mockChatroom.getMember2()).thenReturn(mockOtherMember);

        lenient().when(mockHelpCategory1.getId()).thenReturn(1L);
        lenient().when(mockHelpCategory1.getName()).thenReturn("카테고리1");
        lenient().when(mockHelpCategory2.getId()).thenReturn(2L);
        lenient().when(mockHelpCategory2.getName()).thenReturn("카테고리2");
    }

    @DisplayName("기존 채팅방 ID가 주어지면 해당 채팅방을 조회하고 게시글 연결 없이 반환한다")
    @Test
    void success_OpenExistingChatroomById() {
        // Given
        Long chatroomId = 100L;
        Long currentMemberId = 1L;

        OpenChatroomUseCase.Param param = new OpenChatroomUseCase.Param(
                chatroomId, currentMemberId, null, null, null, null
        );

        when(memberManagement.getExistingMember(currentMemberId)).thenReturn(mockCurrentMember);
        when(chatroomManagement.findChatroomWithMembers(chatroomId)).thenReturn(mockChatroom);

        // When
        OpenChatroomUseCase.Result result = openChatroomUseCase.execute(param);

        // Then
        verify(memberManagement).getExistingMember(currentMemberId);
        verify(chatroomManagement).findChatroomWithMembers(chatroomId);
        verify(chatroomManagement, never()).findChatroomWithMembers(any(MemberSync.class), any(MemberSync.class));
        verify(chatroomManagement, never()).linkPost(any(), any(), any(), any());

        assertThat(result.getChatroomId()).isEqualTo(100L);
        assertThat(result.getMyId()).isEqualTo(1L);
        assertThat(result.getOtherId()).isEqualTo(2L);
        assertThat(result.getOtherNickname()).isEqualTo("OtherUser");
        assertThat(result.getOtherProfileImageUrl()).isEqualTo("other.jpg");
        assertThat(result.getOtherSweetness()).isEqualTo(BigDecimal.valueOf(90.0));
        assertThat(result.getHelpCategories()).isNull();
    }

    @DisplayName("채팅방 ID가 null이면 멤버로 채팅방을 조회/생성하고 게시글을 연결한다")
    @Test
    void success_OpenChatroomByMembers_AndLinkPost() {
        // Given
        Long currentMemberId = 1L;
        Long otherMemberId = 2L;
        Long postId = 999L;
        String postTitle = "도와주세요!";
        List<Long> helpCategoryIds = Arrays.asList(1L, 2L);

        OpenChatroomUseCase.Param param = new OpenChatroomUseCase.Param(
                null, currentMemberId, otherMemberId, postId, postTitle, helpCategoryIds
        );

        when(memberManagement.getExistingMember(currentMemberId)).thenReturn(mockCurrentMember);
        when(memberManagement.getExistingMember(otherMemberId)).thenReturn(mockOtherMember);
        when(chatroomManagement.findChatroomWithMembers(mockCurrentMember, mockOtherMember))
                .thenReturn(mockChatroom);

        // linkPost 후 chatroomHelpCategories가 설정되었다고 가정
        ChatroomHelpCategory mockChatroomHelpCategory1 = mock(ChatroomHelpCategory.class);
        ChatroomHelpCategory mockChatroomHelpCategory2 = mock(ChatroomHelpCategory.class);
        when(mockChatroomHelpCategory1.getHelpCategory()).thenReturn(mockHelpCategory1);
        when(mockChatroomHelpCategory2.getHelpCategory()).thenReturn(mockHelpCategory2);

        List<ChatroomHelpCategory> chatroomHelpCategories = Arrays.asList(
                mockChatroomHelpCategory1, mockChatroomHelpCategory2
        );
        when(mockChatroom.getChatroomHelpCategories()).thenReturn(chatroomHelpCategories);

        // When
        OpenChatroomUseCase.Result result = openChatroomUseCase.execute(param);

        // Then
        verify(memberManagement).getExistingMember(currentMemberId);
        verify(memberManagement).getExistingMember(otherMemberId);
        verify(chatroomManagement).findChatroomWithMembers(mockCurrentMember, mockOtherMember);
        verify(chatroomManagement).linkPost(mockChatroom, postId, postTitle, helpCategoryIds);

        assertThat(result.getChatroomId()).isEqualTo(100L);
        assertThat(result.getMyId()).isEqualTo(1L);
        assertThat(result.getOtherId()).isEqualTo(2L);
        assertThat(result.getOtherNickname()).isEqualTo("OtherUser");
        assertThat(result.getOtherProfileImageUrl()).isEqualTo("other.jpg");
        assertThat(result.getOtherSweetness()).isEqualTo(BigDecimal.valueOf(90.0));
        assertThat(result.getHelpCategories()).hasSize(2);
        assertThat(result.getHelpCategories()).containsExactly(mockHelpCategory1, mockHelpCategory2);
    }
}