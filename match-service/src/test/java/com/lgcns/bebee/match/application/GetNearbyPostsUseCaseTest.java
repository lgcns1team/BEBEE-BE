package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.usecase.GetNearbyPostsUseCase;
import com.lgcns.bebee.match.common.exception.MatchException;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.PostHelpCategory;
import com.lgcns.bebee.match.domain.entity.PostPeriod;
import com.lgcns.bebee.match.domain.entity.PostSchedule;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.entity.vo.LocationSearchType;
import com.lgcns.bebee.match.domain.entity.vo.PostHelpCategoryId;
import com.lgcns.bebee.match.domain.repository.PostRepository;
import com.lgcns.bebee.match.domain.service.MemberManager;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("주변 게시물 조회 유스케이스 테스트")
class GetNearbyPostsUseCaseTest {

    @Mock
    private MemberManager memberManager;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private GetNearbyPostsUseCase useCase;

    private Long currentMemberId;
    private Double latitude;
    private Double longitude;
    private Integer radius;

    @BeforeEach
    void setUp() {
        currentMemberId = 101L;
        latitude = 37.5665;
        longitude = 126.9780;
        radius = 5000;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("HELPER 회원이 HOME 타입으로 주변 게시물을 조회한다")
        void shouldReturnNearbyPosts_whenHelperMemberSearchesWithHomeType() {
            // Given
            GetNearbyPostsUseCase.Param param = new GetNearbyPostsUseCase.Param(
                    currentMemberId, LocationSearchType.HOME, latitude, longitude, radius
            );

            MemberSync mockCurrentMember = mock(MemberSync.class);
            when(mockCurrentMember.getRole()).thenReturn(Role.HELPER);
            when(mockCurrentMember.getLatitude()).thenReturn(latitude);
            when(mockCurrentMember.getLongitude()).thenReturn(longitude);
            when(memberManager.findExistingMember(currentMemberId)).thenReturn(mockCurrentMember);

            Post mockPost = createMockPost(1L, "도움 요청합니다", EngagementType.DAY);
            when(postRepository.findPostsWithRadius(anyDouble(), anyDouble(), anyInt()))
                    .thenReturn(List.of(mockPost));

            // When
            GetNearbyPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNearbyPosts()).hasSize(1);
            verify(postRepository, times(1)).findPostsWithRadius(longitude, latitude, radius);
        }

        @Test
        @DisplayName("HELPER 회원이 CURRENT 타입으로 주변 게시물을 조회한다")
        void shouldReturnNearbyPosts_whenHelperMemberSearchesWithCurrentType() {
            // Given
            Double currentLat = 37.5000;
            Double currentLng = 127.0000;

            GetNearbyPostsUseCase.Param param = new GetNearbyPostsUseCase.Param(
                    currentMemberId, LocationSearchType.CURRENT, currentLat, currentLng, radius
            );

            MemberSync mockCurrentMember = mock(MemberSync.class);
            when(mockCurrentMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(currentMemberId)).thenReturn(mockCurrentMember);

            Post mockPost1 = createMockPost(1L, "도움 요청1", EngagementType.DAY);
            Post mockPost2 = createMockPost(2L, "도움 요청2", EngagementType.TERM);
            when(postRepository.findPostsWithRadius(anyDouble(), anyDouble(), anyInt()))
                    .thenReturn(List.of(mockPost1, mockPost2));

            // When
            GetNearbyPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNearbyPosts()).hasSize(2);
            verify(postRepository, times(1)).findPostsWithRadius(currentLng, currentLat, radius);
        }

        @Test
        @DisplayName("주변에 게시물이 없으면 빈 리스트를 반환한다")
        void shouldReturnEmptyList_whenNoPostsNearby() {
            // Given
            GetNearbyPostsUseCase.Param param = new GetNearbyPostsUseCase.Param(
                    currentMemberId, LocationSearchType.HOME, latitude, longitude, radius
            );

            MemberSync mockCurrentMember = mock(MemberSync.class);
            when(mockCurrentMember.getRole()).thenReturn(Role.HELPER);
            when(mockCurrentMember.getLatitude()).thenReturn(latitude);
            when(mockCurrentMember.getLongitude()).thenReturn(longitude);
            when(memberManager.findExistingMember(currentMemberId)).thenReturn(mockCurrentMember);

            when(postRepository.findPostsWithRadius(anyDouble(), anyDouble(), anyInt()))
                    .thenReturn(List.of());

            // When
            GetNearbyPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNearbyPosts()).isEmpty();
        }

        @Test
        @DisplayName("DAY 타입 게시물의 date가 반환된다")
        void shouldReturnDate_whenPostTypeIsDay() {
            // Given
            GetNearbyPostsUseCase.Param param = new GetNearbyPostsUseCase.Param(
                    currentMemberId, LocationSearchType.HOME, latitude, longitude, radius
            );

            MemberSync mockCurrentMember = mock(MemberSync.class);
            when(mockCurrentMember.getRole()).thenReturn(Role.HELPER);
            when(mockCurrentMember.getLatitude()).thenReturn(latitude);
            when(mockCurrentMember.getLongitude()).thenReturn(longitude);
            when(memberManager.findExistingMember(currentMemberId)).thenReturn(mockCurrentMember);

            Post mockPost = createMockPost(1L, "하루 도움 요청", EngagementType.DAY);
            when(postRepository.findPostsWithRadius(anyDouble(), anyDouble(), anyInt()))
                    .thenReturn(List.of(mockPost));

            // When
            GetNearbyPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getNearbyPosts()).hasSize(1);
            assertThat(result.getNearbyPosts().get(0).getType()).isEqualTo("DAY");
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 검증 실패")
    class BusinessLogicFailures {

        @Test
        @DisplayName("DISABLED 회원이 조회하면 MatchException 발생")
        void shouldThrowException_whenDisabledMemberQueries() {
            // Given
            GetNearbyPostsUseCase.Param param = new GetNearbyPostsUseCase.Param(
                    currentMemberId, LocationSearchType.HOME, latitude, longitude, radius
            );

            MemberSync mockCurrentMember = mock(MemberSync.class);
            when(mockCurrentMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(currentMemberId)).thenReturn(mockCurrentMember);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class)
                    .hasMessageContaining("도우미만");

            verify(postRepository, never()).findPostsWithRadius(anyDouble(), anyDouble(), anyInt());
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("radius가 1일 때 정상 처리된다")
        void shouldWork_whenRadiusIsOne() {
            // Given
            GetNearbyPostsUseCase.Param param = new GetNearbyPostsUseCase.Param(
                    currentMemberId, LocationSearchType.HOME, latitude, longitude, 1
            );

            MemberSync mockCurrentMember = mock(MemberSync.class);
            when(mockCurrentMember.getRole()).thenReturn(Role.HELPER);
            when(mockCurrentMember.getLatitude()).thenReturn(latitude);
            when(mockCurrentMember.getLongitude()).thenReturn(longitude);
            when(memberManager.findExistingMember(currentMemberId)).thenReturn(mockCurrentMember);

            when(postRepository.findPostsWithRadius(anyDouble(), anyDouble(), anyInt()))
                    .thenReturn(List.of());

            // When
            GetNearbyPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(postRepository, times(1)).findPostsWithRadius(longitude, latitude, 1);
        }
    }

    // Helper method to create mock Post
    private Post createMockPost(Long id, String title, EngagementType type) {
        Post mockPost = mock(Post.class);
        PostPeriod mockPeriod = mock(PostPeriod.class);
        PostSchedule mockSchedule = mock(PostSchedule.class);
        PostHelpCategory mockHelpCategory = mock(PostHelpCategory.class);
        PostHelpCategoryId mockHelpCategoryId = mock(PostHelpCategoryId.class);

        when(mockPost.getId()).thenReturn(id);
        when(mockPost.getTitle()).thenReturn(title);
        when(mockPost.getType()).thenReturn(type);
        when(mockPost.getRegion()).thenReturn("서울특별시 중구");
        when(mockPost.getLatitude()).thenReturn(37.5665);
        when(mockPost.getLongitude()).thenReturn(126.9780);

        when(mockPeriod.getStartDate()).thenReturn(LocalDate.of(2025, 1, 15));
        when(mockPost.getPeriod()).thenReturn(mockPeriod);

        when(mockSchedule.getDayOfWeek()).thenReturn(DayOfWeek.MONDAY);
        when(mockPost.getSchedules()).thenReturn(List.of(mockSchedule));

        when(mockHelpCategoryId.getHelpCategoryId()).thenReturn(1L);
        when(mockHelpCategory.getId()).thenReturn(mockHelpCategoryId);
        when(mockPost.getHelpCategories()).thenReturn(List.of(mockHelpCategory));

        return mockPost;
    }
}
