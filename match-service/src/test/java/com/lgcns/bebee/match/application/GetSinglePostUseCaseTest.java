package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.usecase.GetSinglePostUseCase;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.PostHelpCategory;
import com.lgcns.bebee.match.domain.entity.PostImage;
import com.lgcns.bebee.match.domain.entity.PostPeriod;
import com.lgcns.bebee.match.domain.entity.PostSchedule;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.entity.vo.PostHelpCategoryId;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.PostManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("단일 게시물 조회 유스케이스 테스트")
class GetSinglePostUseCaseTest {

    @Mock
    private PostManager postManager;

    @Mock
    private MemberManager memberManager;

    @InjectMocks
    private GetSinglePostUseCase useCase;

    private Long memberId;
    private Long postId;

    @BeforeEach
    void setUp() {
        memberId = 101L;
        postId = 1L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("DAY 타입 게시물을 조회하면 date가 반환된다")
        void shouldReturnDate_whenPostTypeIsDay() {
            // Given
            GetSinglePostUseCase.Param param = new GetSinglePostUseCase.Param(memberId, postId);

            Post mockPost = createMockPost(postId, EngagementType.DAY);
            MemberSync mockMember = createMockMember(201L);

            when(postManager.findSinglePost(postId)).thenReturn(mockPost);
            when(memberManager.findExistingMember(201L)).thenReturn(mockMember);

            // When
            GetSinglePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getEngagementType()).isEqualTo("DAY");
            assertThat(result.getDate()).isEqualTo(LocalDate.of(2025, 1, 15));
            assertThat(result.getStartDate()).isNull();
            assertThat(result.getEndDate()).isNull();
            verify(postManager, times(1)).findSinglePost(postId);
        }

        @Test
        @DisplayName("TERM 타입 게시물을 조회하면 startDate와 endDate가 반환된다")
        void shouldReturnStartAndEndDate_whenPostTypeIsTerm() {
            // Given
            GetSinglePostUseCase.Param param = new GetSinglePostUseCase.Param(memberId, postId);

            Post mockPost = createMockPostWithTerm(postId);
            MemberSync mockMember = createMockMember(201L);

            when(postManager.findSinglePost(postId)).thenReturn(mockPost);
            when(memberManager.findExistingMember(201L)).thenReturn(mockMember);

            // When
            GetSinglePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getEngagementType()).isEqualTo("TERM");
            assertThat(result.getDate()).isNull();
            assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2025, 1, 1));
            assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2025, 3, 31));
        }

        @Test
        @DisplayName("게시물 작성자 정보가 포함되어 반환된다")
        void shouldReturnMemberInfo() {
            // Given
            GetSinglePostUseCase.Param param = new GetSinglePostUseCase.Param(memberId, postId);

            Post mockPost = createMockPost(postId, EngagementType.DAY);
            MemberSync mockMember = createMockMember(201L);

            when(postManager.findSinglePost(postId)).thenReturn(mockPost);
            when(memberManager.findExistingMember(201L)).thenReturn(mockMember);

            // When
            GetSinglePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getMemberId()).isEqualTo(201L);
            assertThat(result.getMemberNickname()).isEqualTo("테스트유저");
            assertThat(result.getMemberAddress()).isEqualTo("서울특별시 중구 세종대로 110");
            assertThat(result.getMemberProfileImageUrl()).isEqualTo("https://example.com/profile.jpg");
        }

        @Test
        @DisplayName("게시물의 스케줄 정보가 포함되어 반환된다")
        void shouldReturnScheduleInfo() {
            // Given
            GetSinglePostUseCase.Param param = new GetSinglePostUseCase.Param(memberId, postId);

            Post mockPost = createMockPost(postId, EngagementType.DAY);
            MemberSync mockMember = createMockMember(201L);

            when(postManager.findSinglePost(postId)).thenReturn(mockPost);
            when(memberManager.findExistingMember(201L)).thenReturn(mockMember);

            // When
            GetSinglePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getSchedules()).hasSize(1);
            assertThat(result.getSchedules().get(0).getDayOfWeek()).isEqualTo("MONDAY");
            assertThat(result.getSchedules().get(0).getStartTime()).isEqualTo(LocalTime.of(9, 0));
            assertThat(result.getSchedules().get(0).getEndTime()).isEqualTo(LocalTime.of(12, 0));
        }

        @Test
        @DisplayName("게시물의 이미지 URL 목록이 반환된다")
        void shouldReturnPostImageUrls() {
            // Given
            GetSinglePostUseCase.Param param = new GetSinglePostUseCase.Param(memberId, postId);

            Post mockPost = createMockPost(postId, EngagementType.DAY);
            MemberSync mockMember = createMockMember(201L);

            when(postManager.findSinglePost(postId)).thenReturn(mockPost);
            when(memberManager.findExistingMember(201L)).thenReturn(mockMember);

            // When
            GetSinglePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getPostImageUrls()).hasSize(2);
            assertThat(result.getPostImageUrls()).containsExactly(
                    "https://example.com/image1.jpg",
                    "https://example.com/image2.jpg"
            );
        }

        @Test
        @DisplayName("게시물의 도움 카테고리 ID 목록이 반환된다")
        void shouldReturnHelpCategoryIds() {
            // Given
            GetSinglePostUseCase.Param param = new GetSinglePostUseCase.Param(memberId, postId);

            Post mockPost = createMockPost(postId, EngagementType.DAY);
            MemberSync mockMember = createMockMember(201L);

            when(postManager.findSinglePost(postId)).thenReturn(mockPost);
            when(memberManager.findExistingMember(201L)).thenReturn(mockMember);

            // When
            GetSinglePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getHelpCategoryIds()).containsExactly(1L, 2L);
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("postId가 1일 때 정상 처리된다")
        void shouldWork_whenPostIdIsOne() {
            // Given
            GetSinglePostUseCase.Param param = new GetSinglePostUseCase.Param(memberId, 1L);

            Post mockPost = createMockPost(1L, EngagementType.DAY);
            MemberSync mockMember = createMockMember(201L);

            when(postManager.findSinglePost(1L)).thenReturn(mockPost);
            when(memberManager.findExistingMember(201L)).thenReturn(mockMember);

            // When
            GetSinglePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(postManager, times(1)).findSinglePost(1L);
        }

        @Test
        @DisplayName("이미지가 없는 게시물도 정상 조회된다")
        void shouldWork_whenNoImages() {
            // Given
            GetSinglePostUseCase.Param param = new GetSinglePostUseCase.Param(memberId, postId);

            Post mockPost = createMockPostWithoutImages(postId);
            MemberSync mockMember = createMockMember(201L);

            when(postManager.findSinglePost(postId)).thenReturn(mockPost);
            when(memberManager.findExistingMember(201L)).thenReturn(mockMember);

            // When
            GetSinglePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getPostImageUrls()).isEmpty();
        }

        @Test
        @DisplayName("unitHoney가 0인 게시물도 정상 조회된다")
        void shouldWork_whenUnitHoneyIsZero() {
            // Given
            GetSinglePostUseCase.Param param = new GetSinglePostUseCase.Param(memberId, postId);

            Post mockPost = createMockPostWithZeroHoney(postId);
            MemberSync mockMember = createMockMember(201L);

            when(postManager.findSinglePost(postId)).thenReturn(mockPost);
            when(memberManager.findExistingMember(201L)).thenReturn(mockMember);

            // When
            GetSinglePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getUnitHoney()).isEqualTo(0L);
            assertThat(result.getTotalHoney()).isEqualTo(0L);
        }
    }

    // Helper methods
    private Post createMockPost(Long id, EngagementType type) {
        Post mockPost = mock(Post.class);
        PostPeriod mockPeriod = mock(PostPeriod.class);
        PostSchedule mockSchedule = mock(PostSchedule.class);
        PostHelpCategory mockHelpCategory1 = mock(PostHelpCategory.class);
        PostHelpCategory mockHelpCategory2 = mock(PostHelpCategory.class);
        PostHelpCategoryId mockHelpCategoryId1 = mock(PostHelpCategoryId.class);
        PostHelpCategoryId mockHelpCategoryId2 = mock(PostHelpCategoryId.class);
        PostImage mockImage1 = mock(PostImage.class);
        PostImage mockImage2 = mock(PostImage.class);

        when(mockPost.getId()).thenReturn(id);
        when(mockPost.getMemberId()).thenReturn(201L);
        when(mockPost.getType()).thenReturn(type);
        when(mockPost.getTitle()).thenReturn("도움 요청합니다");
        when(mockPost.getContent()).thenReturn("도움이 필요합니다.");
        when(mockPost.getUnitHoney()).thenReturn(5000L);
        when(mockPost.getTotalHoney()).thenReturn(5000L);
        when(mockPost.getRegion()).thenReturn("서울특별시 중구");
        when(mockPost.getApplicantCount()).thenReturn(3);

        when(mockPeriod.getStartDate()).thenReturn(LocalDate.of(2025, 1, 15));
        when(mockPeriod.getEndDate()).thenReturn(LocalDate.of(2025, 1, 15));
        when(mockPost.getPeriod()).thenReturn(mockPeriod);

        when(mockSchedule.getDayOfWeek()).thenReturn(DayOfWeek.MONDAY);
        when(mockSchedule.getStartTime()).thenReturn(LocalTime.of(9, 0));
        when(mockSchedule.getEndTime()).thenReturn(LocalTime.of(12, 0));
        when(mockPost.getSchedules()).thenReturn(List.of(mockSchedule));

        when(mockHelpCategoryId1.getHelpCategoryId()).thenReturn(1L);
        when(mockHelpCategoryId2.getHelpCategoryId()).thenReturn(2L);
        when(mockHelpCategory1.getId()).thenReturn(mockHelpCategoryId1);
        when(mockHelpCategory2.getId()).thenReturn(mockHelpCategoryId2);
        when(mockPost.getHelpCategories()).thenReturn(List.of(mockHelpCategory1, mockHelpCategory2));

        when(mockImage1.getImageUrl()).thenReturn("https://example.com/image1.jpg");
        when(mockImage1.getSequence()).thenReturn(0);
        when(mockImage2.getImageUrl()).thenReturn("https://example.com/image2.jpg");
        when(mockImage2.getSequence()).thenReturn(1);
        when(mockPost.getImages()).thenReturn(List.of(mockImage1, mockImage2));

        return mockPost;
    }

    private Post createMockPostWithTerm(Long id) {
        Post mockPost = createMockPost(id, EngagementType.TERM);
        PostPeriod mockPeriod = mock(PostPeriod.class);

        when(mockPeriod.getStartDate()).thenReturn(LocalDate.of(2025, 1, 1));
        when(mockPeriod.getEndDate()).thenReturn(LocalDate.of(2025, 3, 31));
        when(mockPost.getPeriod()).thenReturn(mockPeriod);

        return mockPost;
    }

    private Post createMockPostWithoutImages(Long id) {
        Post mockPost = createMockPost(id, EngagementType.DAY);
        when(mockPost.getImages()).thenReturn(List.of());
        return mockPost;
    }

    private Post createMockPostWithZeroHoney(Long id) {
        Post mockPost = createMockPost(id, EngagementType.DAY);
        when(mockPost.getUnitHoney()).thenReturn(0L);
        when(mockPost.getTotalHoney()).thenReturn(0L);
        return mockPost;
    }

    private MemberSync createMockMember(Long id) {
        MemberSync mockMember = mock(MemberSync.class);
        when(mockMember.getId()).thenReturn(id);
        when(mockMember.getNickname()).thenReturn("테스트유저");
        when(mockMember.getAddressRoad()).thenReturn("서울특별시 중구 세종대로 110");
        when(mockMember.getProfileImageUrl()).thenReturn("https://example.com/profile.jpg");
        return mockMember;
    }
}
