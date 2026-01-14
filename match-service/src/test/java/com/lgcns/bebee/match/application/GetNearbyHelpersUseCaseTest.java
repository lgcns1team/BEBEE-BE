package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.usecase.GetNearbyHelpersUseCase;
import com.lgcns.bebee.match.common.exception.MatchException;
import com.lgcns.bebee.match.domain.entity.sync.BadgeSync;
import com.lgcns.bebee.match.domain.entity.sync.Gender;
import com.lgcns.bebee.match.domain.entity.sync.MemberHelpCategorySync;
import com.lgcns.bebee.match.domain.entity.sync.MemberHelpCategorySyncId;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.LocationSearchType;
import com.lgcns.bebee.match.domain.repository.MemberRepository;
import com.lgcns.bebee.match.domain.service.BadgeManager;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("주변 도우미 조회 유스케이스 테스트")
class GetNearbyHelpersUseCaseTest {

    @Mock
    private MemberManager memberManager;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BadgeManager badgeManager;

    @InjectMocks
    private GetNearbyHelpersUseCase useCase;

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
        @DisplayName("DISABLED 회원이 HOME 타입으로 주변 도우미를 조회한다")
        void shouldReturnNearbyHelpers_whenDisabledMemberSearchesWithHomeType() {
            // Given
            GetNearbyHelpersUseCase.Param param = new GetNearbyHelpersUseCase.Param(
                    currentMemberId, LocationSearchType.HOME, latitude, longitude, radius
            );

            MemberSync mockCurrentMember = mock(MemberSync.class);
            when(mockCurrentMember.getRole()).thenReturn(Role.DISABLED);
            when(mockCurrentMember.getLatitude()).thenReturn(latitude);
            when(mockCurrentMember.getLongitude()).thenReturn(longitude);
            when(memberManager.findExistingMember(currentMemberId)).thenReturn(mockCurrentMember);

            MemberSync mockHelper = createMockHelper(201L, "도우미1");
            when(memberRepository.findHelpersWithinRadius(anyDouble(), anyDouble(), anyInt()))
                    .thenReturn(List.of(mockHelper));

            when(badgeManager.findBadgesByHelperId(anyLong())).thenReturn(List.of());

            // When
            GetNearbyHelpersUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNearbyHelpers()).hasSize(1);
            verify(memberRepository, times(1)).findHelpersWithinRadius(longitude, latitude, radius);
        }

        @Test
        @DisplayName("DISABLED 회원이 CURRENT 타입으로 주변 도우미를 조회한다")
        void shouldReturnNearbyHelpers_whenDisabledMemberSearchesWithCurrentType() {
            // Given
            Double currentLat = 37.5000;
            Double currentLng = 127.0000;

            GetNearbyHelpersUseCase.Param param = new GetNearbyHelpersUseCase.Param(
                    currentMemberId, LocationSearchType.CURRENT, currentLat, currentLng, radius
            );

            MemberSync mockCurrentMember = mock(MemberSync.class);
            when(mockCurrentMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(currentMemberId)).thenReturn(mockCurrentMember);

            MemberSync mockHelper1 = createMockHelper(201L, "도우미1");
            MemberSync mockHelper2 = createMockHelper(202L, "도우미2");
            when(memberRepository.findHelpersWithinRadius(anyDouble(), anyDouble(), anyInt()))
                    .thenReturn(List.of(mockHelper1, mockHelper2));

            when(badgeManager.findBadgesByHelperId(anyLong())).thenReturn(List.of());

            // When
            GetNearbyHelpersUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNearbyHelpers()).hasSize(2);
            verify(memberRepository, times(1)).findHelpersWithinRadius(currentLng, currentLat, radius);
        }

        @Test
        @DisplayName("도우미의 뱃지 정보가 포함되어 반환된다")
        void shouldReturnHelpersWithBadges() {
            // Given
            GetNearbyHelpersUseCase.Param param = new GetNearbyHelpersUseCase.Param(
                    currentMemberId, LocationSearchType.HOME, latitude, longitude, radius
            );

            MemberSync mockCurrentMember = mock(MemberSync.class);
            when(mockCurrentMember.getRole()).thenReturn(Role.DISABLED);
            when(mockCurrentMember.getLatitude()).thenReturn(latitude);
            when(mockCurrentMember.getLongitude()).thenReturn(longitude);
            when(memberManager.findExistingMember(currentMemberId)).thenReturn(mockCurrentMember);

            MemberSync mockHelper = createMockHelper(201L, "도우미1");
            when(memberRepository.findHelpersWithinRadius(anyDouble(), anyDouble(), anyInt()))
                    .thenReturn(List.of(mockHelper));

            BadgeSync mockBadge = mock(BadgeSync.class);
            when(mockBadge.getDisabilityCategoryId()).thenReturn(1L);
            when(mockBadge.getCompletionCount()).thenReturn(10);
            when(mockBadge.getBadgeCode()).thenReturn("BRONZE");
            when(badgeManager.findBadgesByHelperId(201L)).thenReturn(List.of(mockBadge));

            // When
            GetNearbyHelpersUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getNearbyHelpers()).hasSize(1);
            assertThat(result.getNearbyHelpers().get(0).getBadges()).hasSize(1);
        }

        @Test
        @DisplayName("주변에 도우미가 없으면 빈 리스트를 반환한다")
        void shouldReturnEmptyList_whenNoHelpersNearby() {
            // Given
            GetNearbyHelpersUseCase.Param param = new GetNearbyHelpersUseCase.Param(
                    currentMemberId, LocationSearchType.HOME, latitude, longitude, radius
            );

            MemberSync mockCurrentMember = mock(MemberSync.class);
            when(mockCurrentMember.getRole()).thenReturn(Role.DISABLED);
            when(mockCurrentMember.getLatitude()).thenReturn(latitude);
            when(mockCurrentMember.getLongitude()).thenReturn(longitude);
            when(memberManager.findExistingMember(currentMemberId)).thenReturn(mockCurrentMember);

            when(memberRepository.findHelpersWithinRadius(anyDouble(), anyDouble(), anyInt()))
                    .thenReturn(List.of());

            // When
            GetNearbyHelpersUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getNearbyHelpers()).isEmpty();
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 검증 실패")
    class BusinessLogicFailures {

        @Test
        @DisplayName("HELPER 회원이 조회하면 MatchException 발생")
        void shouldThrowException_whenHelperMemberQueries() {
            // Given
            GetNearbyHelpersUseCase.Param param = new GetNearbyHelpersUseCase.Param(
                    currentMemberId, LocationSearchType.HOME, latitude, longitude, radius
            );

            MemberSync mockCurrentMember = mock(MemberSync.class);
            when(mockCurrentMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(currentMemberId)).thenReturn(mockCurrentMember);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class)
                    .hasMessageContaining("장애인 회원만");

            verify(memberRepository, never()).findHelpersWithinRadius(anyDouble(), anyDouble(), anyInt());
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("radius가 1일 때 정상 처리된다")
        void shouldWork_whenRadiusIsOne() {
            // Given
            GetNearbyHelpersUseCase.Param param = new GetNearbyHelpersUseCase.Param(
                    currentMemberId, LocationSearchType.HOME, latitude, longitude, 1
            );

            MemberSync mockCurrentMember = mock(MemberSync.class);
            when(mockCurrentMember.getRole()).thenReturn(Role.DISABLED);
            when(mockCurrentMember.getLatitude()).thenReturn(latitude);
            when(mockCurrentMember.getLongitude()).thenReturn(longitude);
            when(memberManager.findExistingMember(currentMemberId)).thenReturn(mockCurrentMember);

            when(memberRepository.findHelpersWithinRadius(anyDouble(), anyDouble(), anyInt()))
                    .thenReturn(List.of());

            // When
            GetNearbyHelpersUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(memberRepository, times(1)).findHelpersWithinRadius(longitude, latitude, 1);
        }
    }

    // Helper method to create mock MemberSync
    private MemberSync createMockHelper(Long id, String nickname) {
        MemberSync mockHelper = mock(MemberSync.class);
        MemberHelpCategorySync mockHelpCategory = mock(MemberHelpCategorySync.class);
        MemberHelpCategorySyncId mockHelpCategoryId = mock(MemberHelpCategorySyncId.class);

        when(mockHelper.getId()).thenReturn(id);
        when(mockHelper.getNickname()).thenReturn(nickname);
        when(mockHelper.getGender()).thenReturn(Gender.MALE);
        when(mockHelper.getBirthDate()).thenReturn(LocalDate.of(1990, 1, 1));
        when(mockHelper.getLatitude()).thenReturn(37.5665);
        when(mockHelper.getLongitude()).thenReturn(126.9780);
        when(mockHelpCategoryId.getHelpCategoryId()).thenReturn(1L);
        when(mockHelpCategory.getId()).thenReturn(mockHelpCategoryId);
        when(mockHelper.getHelpCategories()).thenReturn(List.of(mockHelpCategory));

        return mockHelper;
    }
}
