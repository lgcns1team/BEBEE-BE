package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.usecase.GetPostsUseCase;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.PostHelpCategory;
import com.lgcns.bebee.match.domain.entity.PostImage;
import com.lgcns.bebee.match.domain.entity.PostPeriod;
import com.lgcns.bebee.match.domain.entity.PostSchedule;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.entity.vo.PostHelpCategoryId;
import com.lgcns.bebee.match.domain.entity.vo.PostStatus;
import com.lgcns.bebee.match.domain.repository.PostRepository;
import com.lgcns.bebee.match.domain.repository.dto.PostSearchCond;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("게시물 목록 조회 유스케이스 테스트")
class GetPostsUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private GetPostsUseCase useCase;

    private Long currentMemberId;

    @BeforeEach
    void setUp() {
        currentMemberId = 101L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("필터 없이 게시물 목록을 조회한다")
        void shouldReturnPosts_whenNoFiltersApplied() {
            // Given
            GetPostsUseCase.Param param = new GetPostsUseCase.Param(
                    currentMemberId,
                    null, // postType
                    null, // isMatched
                    null, // legalDongCodes
                    null, // helpCategoryIds
                    null, // gender
                    null, // minHoney
                    null, // maxHoney
                    null, // disabilityCategoryIds
                    null, // days
                    null, // lastPostId
                    10    // count
            );

            List<Post> mockPosts = List.of(
                    createMockPost(1L, "게시물1", EngagementType.DAY),
                    createMockPost(2L, "게시물2", EngagementType.TERM)
            );

            when(postRepository.findPosts(any(PostSearchCond.class))).thenReturn(mockPosts);

            // When
            GetPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getPosts()).hasSize(2);
            assertThat(result.getHasNext()).isFalse();
            verify(postRepository, times(1)).findPosts(any(PostSearchCond.class));
        }

        @Test
        @DisplayName("DAY 타입 필터로 게시물을 조회한다")
        void shouldReturnPosts_whenDayTypeFilterApplied() {
            // Given
            GetPostsUseCase.Param param = new GetPostsUseCase.Param(
                    currentMemberId,
                    "DAY",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    10
            );

            List<Post> mockPosts = List.of(createMockPost(1L, "하루 도움", EngagementType.DAY));
            when(postRepository.findPosts(any(PostSearchCond.class))).thenReturn(mockPosts);

            ArgumentCaptor<PostSearchCond> captor = ArgumentCaptor.forClass(PostSearchCond.class);

            // When
            GetPostsUseCase.Result result = useCase.execute(param);

            // Then
            verify(postRepository).findPosts(captor.capture());
            PostSearchCond cond = captor.getValue();
            assertThat(cond.engagementType()).isEqualTo(EngagementType.DAY);
            assertThat(result.getPosts()).hasSize(1);
        }

        @Test
        @DisplayName("다음 페이지가 있으면 hasNext가 true를 반환한다")
        void shouldReturnHasNextTrue_whenMorePostsExist() {
            // Given
            GetPostsUseCase.Param param = new GetPostsUseCase.Param(
                    currentMemberId,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    2 // count=2 이므로 3개 이상이면 다음 페이지 있음
            );

            List<Post> mockPosts = List.of(
                    createMockPost(3L, "게시물3", EngagementType.DAY),
                    createMockPost(2L, "게시물2", EngagementType.DAY),
                    createMockPost(1L, "게시물1", EngagementType.DAY) // 3개 = count+1
            );

            when(postRepository.findPosts(any(PostSearchCond.class))).thenReturn(mockPosts);

            // When
            GetPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getHasNext()).isTrue();
            assertThat(result.getNextPostId()).isEqualTo(1L);
            assertThat(result.getPosts()).hasSize(2); // count만큼만 반환
        }

        @Test
        @DisplayName("커서 기반 페이징으로 게시물을 조회한다")
        void shouldReturnPosts_withCursorBasedPagination() {
            // Given
            GetPostsUseCase.Param param = new GetPostsUseCase.Param(
                    currentMemberId,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    100L, // lastPostId
                    10
            );

            List<Post> mockPosts = List.of(createMockPost(99L, "게시물99", EngagementType.DAY));
            when(postRepository.findPosts(any(PostSearchCond.class))).thenReturn(mockPosts);

            ArgumentCaptor<PostSearchCond> captor = ArgumentCaptor.forClass(PostSearchCond.class);

            // When
            GetPostsUseCase.Result result = useCase.execute(param);

            // Then
            verify(postRepository).findPosts(captor.capture());
            PostSearchCond cond = captor.getValue();
            assertThat(cond.lastPostId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("지역 코드 필터로 게시물을 조회한다")
        void shouldReturnPosts_whenLegalDongCodeFilterApplied() {
            // Given
            List<String> legalDongCodes = List.of("1100000000", "1100000001");

            GetPostsUseCase.Param param = new GetPostsUseCase.Param(
                    currentMemberId,
                    null,
                    null,
                    legalDongCodes,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    10
            );

            List<Post> mockPosts = List.of(createMockPost(1L, "서울 게시물", EngagementType.DAY));
            when(postRepository.findPosts(any(PostSearchCond.class))).thenReturn(mockPosts);

            ArgumentCaptor<PostSearchCond> captor = ArgumentCaptor.forClass(PostSearchCond.class);

            // When
            GetPostsUseCase.Result result = useCase.execute(param);

            // Then
            verify(postRepository).findPosts(captor.capture());
            PostSearchCond cond = captor.getValue();
            assertThat(cond.legalDongCode()).containsExactlyInAnyOrderElementsOf(legalDongCodes);
        }

        @Test
        @DisplayName("요일 필터로 게시물을 조회한다")
        void shouldReturnPosts_whenDayOfWeekFilterApplied() {
            // Given
            List<String> days = List.of("MONDAY", "WEDNESDAY");

            GetPostsUseCase.Param param = new GetPostsUseCase.Param(
                    currentMemberId,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    days,
                    null,
                    10
            );

            List<Post> mockPosts = List.of(createMockPost(1L, "월수 게시물", EngagementType.TERM));
            when(postRepository.findPosts(any(PostSearchCond.class))).thenReturn(mockPosts);

            ArgumentCaptor<PostSearchCond> captor = ArgumentCaptor.forClass(PostSearchCond.class);

            // When
            GetPostsUseCase.Result result = useCase.execute(param);

            // Then
            verify(postRepository).findPosts(captor.capture());
            PostSearchCond cond = captor.getValue();
            assertThat(cond.dayOfWeeks()).containsExactly(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("게시물이 없으면 빈 리스트를 반환한다")
        void shouldReturnEmptyList_whenNoPostsFound() {
            // Given
            GetPostsUseCase.Param param = new GetPostsUseCase.Param(
                    currentMemberId,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    10
            );

            when(postRepository.findPosts(any(PostSearchCond.class))).thenReturn(List.of());

            // When
            GetPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getPosts()).isEmpty();
            assertThat(result.getHasNext()).isFalse();
            assertThat(result.getNextPostId()).isNull();
        }

        @Test
        @DisplayName("count가 1일 때 정상 처리된다")
        void shouldWork_whenCountIsOne() {
            // Given
            GetPostsUseCase.Param param = new GetPostsUseCase.Param(
                    currentMemberId,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    1
            );

            List<Post> mockPosts = List.of(
                    createMockPost(2L, "게시물2", EngagementType.DAY),
                    createMockPost(1L, "게시물1", EngagementType.DAY)
            );
            when(postRepository.findPosts(any(PostSearchCond.class))).thenReturn(mockPosts);

            // When
            GetPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getPosts()).hasSize(1);
            assertThat(result.getHasNext()).isTrue();
        }

        @Test
        @DisplayName("minHoney와 maxHoney 필터가 적용된다")
        void shouldApplyHoneyRangeFilter() {
            // Given
            GetPostsUseCase.Param param = new GetPostsUseCase.Param(
                    currentMemberId,
                    null,
                    null,
                    null,
                    null,
                    null,
                    1000L,  // minHoney
                    10000L, // maxHoney
                    null,
                    null,
                    null,
                    10
            );

            List<Post> mockPosts = List.of(createMockPost(1L, "게시물", EngagementType.DAY));
            when(postRepository.findPosts(any(PostSearchCond.class))).thenReturn(mockPosts);

            ArgumentCaptor<PostSearchCond> captor = ArgumentCaptor.forClass(PostSearchCond.class);

            // When
            GetPostsUseCase.Result result = useCase.execute(param);

            // Then
            verify(postRepository).findPosts(captor.capture());
            PostSearchCond cond = captor.getValue();
            assertThat(cond.minHoney()).isEqualTo(1000L);
            assertThat(cond.maxHoney()).isEqualTo(10000L);
        }
    }

    // Helper method to create mock Post
    private Post createMockPost(Long id, String title, EngagementType type) {
        Post mockPost = mock(Post.class);
        PostPeriod mockPeriod = mock(PostPeriod.class);
        PostSchedule mockSchedule = mock(PostSchedule.class);
        PostHelpCategory mockHelpCategory = mock(PostHelpCategory.class);
        PostHelpCategoryId mockHelpCategoryId = mock(PostHelpCategoryId.class);
        PostImage mockImage = mock(PostImage.class);

        when(mockPost.getId()).thenReturn(id);
        when(mockPost.getTitle()).thenReturn(title);
        when(mockPost.getType()).thenReturn(type);
        when(mockPost.getStatus()).thenReturn(PostStatus.NON_MATCHED);
        when(mockPost.getUnitHoney()).thenReturn(5000L);
        when(mockPost.getTotalHoney()).thenReturn(5000L);
        when(mockPost.getRegion()).thenReturn("서울특별시 중구");

        when(mockPeriod.getStartDate()).thenReturn(LocalDate.of(2025, 1, 15));
        when(mockPost.getPeriod()).thenReturn(mockPeriod);

        when(mockSchedule.getDayOfWeek()).thenReturn(DayOfWeek.MONDAY);
        when(mockPost.getSchedules()).thenReturn(List.of(mockSchedule));

        when(mockHelpCategoryId.getHelpCategoryId()).thenReturn(1L);
        when(mockHelpCategory.getId()).thenReturn(mockHelpCategoryId);
        when(mockPost.getHelpCategories()).thenReturn(List.of(mockHelpCategory));

        when(mockImage.getImageUrl()).thenReturn("https://example.com/image.jpg");
        when(mockPost.getImages()).thenReturn(List.of(mockImage));

        return mockPost;
    }
}
