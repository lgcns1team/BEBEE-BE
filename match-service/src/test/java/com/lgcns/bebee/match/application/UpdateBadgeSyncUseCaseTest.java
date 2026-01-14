package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.usecase.UpdateBadgeSyncUseCase;
import com.lgcns.bebee.match.domain.entity.sync.BadgeSync;
import com.lgcns.bebee.match.domain.repository.BadgeSyncRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("뱃지 동기화 업데이트 유스케이스 테스트")
class UpdateBadgeSyncUseCaseTest {

    @Mock
    private BadgeSyncRepository badgeSyncRepository;

    @InjectMocks
    private UpdateBadgeSyncUseCase useCase;

    private Long helperId;

    @BeforeEach
    void setUp() {
        helperId = 101L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("새로운 뱃지 정보가 생성된다")
        void shouldCreateNewBadgeSync_whenNotExists() {
            // Given
            UpdateBadgeSyncUseCase.BadgeInfo badgeInfo = new UpdateBadgeSyncUseCase.BadgeInfo(
                    1L, 10, "BRONZE"
            );
            UpdateBadgeSyncUseCase.Param param = new UpdateBadgeSyncUseCase.Param(
                    helperId, List.of(badgeInfo)
            );

            when(badgeSyncRepository.findByHelperIdAndDisabilityCategoryId(helperId, 1L))
                    .thenReturn(Optional.empty());

            BadgeSync mockBadgeSync = mock(BadgeSync.class);
            when(badgeSyncRepository.save(any(BadgeSync.class))).thenReturn(mockBadgeSync);

            // When
            UpdateBadgeSyncUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMessage()).contains("뱃지 동기화 완료");
            verify(badgeSyncRepository, times(1)).findByHelperIdAndDisabilityCategoryId(helperId, 1L);
            verify(badgeSyncRepository, times(1)).save(any(BadgeSync.class));
        }

        @Test
        @DisplayName("기존 뱃지 정보가 업데이트된다")
        void shouldUpdateExistingBadgeSync_whenExists() {
            // Given
            UpdateBadgeSyncUseCase.BadgeInfo badgeInfo = new UpdateBadgeSyncUseCase.BadgeInfo(
                    1L, 20, "SILVER"
            );
            UpdateBadgeSyncUseCase.Param param = new UpdateBadgeSyncUseCase.Param(
                    helperId, List.of(badgeInfo)
            );

            BadgeSync existingBadgeSync = mock(BadgeSync.class);
            when(badgeSyncRepository.findByHelperIdAndDisabilityCategoryId(helperId, 1L))
                    .thenReturn(Optional.of(existingBadgeSync));
            when(badgeSyncRepository.save(any(BadgeSync.class))).thenReturn(existingBadgeSync);

            // When
            UpdateBadgeSyncUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(existingBadgeSync, times(1)).updateBadge(20, "SILVER");
            verify(badgeSyncRepository, times(1)).save(existingBadgeSync);
        }

        @Test
        @DisplayName("여러 뱃지 정보가 한 번에 처리된다")
        void shouldProcessMultipleBadges() {
            // Given
            List<UpdateBadgeSyncUseCase.BadgeInfo> badges = List.of(
                    new UpdateBadgeSyncUseCase.BadgeInfo(1L, 10, "BRONZE"),
                    new UpdateBadgeSyncUseCase.BadgeInfo(2L, 25, "SILVER"),
                    new UpdateBadgeSyncUseCase.BadgeInfo(3L, 50, "GOLD")
            );
            UpdateBadgeSyncUseCase.Param param = new UpdateBadgeSyncUseCase.Param(helperId, badges);

            when(badgeSyncRepository.findByHelperIdAndDisabilityCategoryId(anyLong(), anyLong()))
                    .thenReturn(Optional.empty());

            BadgeSync mockBadgeSync = mock(BadgeSync.class);
            when(badgeSyncRepository.save(any(BadgeSync.class))).thenReturn(mockBadgeSync);

            // When
            UpdateBadgeSyncUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(badgeSyncRepository, times(3)).findByHelperIdAndDisabilityCategoryId(anyLong(), anyLong());
            verify(badgeSyncRepository, times(3)).save(any(BadgeSync.class));
        }

        @Test
        @DisplayName("빈 뱃지 리스트도 정상 처리된다")
        void shouldHandleEmptyBadgeList() {
            // Given
            UpdateBadgeSyncUseCase.Param param = new UpdateBadgeSyncUseCase.Param(
                    helperId, List.of()
            );

            // When
            UpdateBadgeSyncUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMessage()).contains("뱃지 동기화 완료");
            verify(badgeSyncRepository, never()).findByHelperIdAndDisabilityCategoryId(anyLong(), anyLong());
            verify(badgeSyncRepository, never()).save(any(BadgeSync.class));
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("helperId가 1일 때 정상 처리된다")
        void shouldWork_whenHelperIdIsOne() {
            // Given
            UpdateBadgeSyncUseCase.BadgeInfo badgeInfo = new UpdateBadgeSyncUseCase.BadgeInfo(
                    1L, 5, "BRONZE"
            );
            UpdateBadgeSyncUseCase.Param param = new UpdateBadgeSyncUseCase.Param(
                    1L, List.of(badgeInfo)
            );

            when(badgeSyncRepository.findByHelperIdAndDisabilityCategoryId(1L, 1L))
                    .thenReturn(Optional.empty());

            BadgeSync mockBadgeSync = mock(BadgeSync.class);
            when(badgeSyncRepository.save(any(BadgeSync.class))).thenReturn(mockBadgeSync);

            // When
            UpdateBadgeSyncUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(badgeSyncRepository, times(1)).findByHelperIdAndDisabilityCategoryId(1L, 1L);
        }

        @Test
        @DisplayName("completionCount가 0일 때 정상 처리된다")
        void shouldWork_whenCompletionCountIsZero() {
            // Given
            UpdateBadgeSyncUseCase.BadgeInfo badgeInfo = new UpdateBadgeSyncUseCase.BadgeInfo(
                    1L, 0, "NONE"
            );
            UpdateBadgeSyncUseCase.Param param = new UpdateBadgeSyncUseCase.Param(
                    helperId, List.of(badgeInfo)
            );

            when(badgeSyncRepository.findByHelperIdAndDisabilityCategoryId(helperId, 1L))
                    .thenReturn(Optional.empty());

            BadgeSync mockBadgeSync = mock(BadgeSync.class);
            when(badgeSyncRepository.save(any(BadgeSync.class))).thenReturn(mockBadgeSync);

            // When
            UpdateBadgeSyncUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(badgeSyncRepository, times(1)).save(any(BadgeSync.class));
        }

        @Test
        @DisplayName("단일 뱃지 업데이트가 정상 처리된다")
        void shouldWork_withSingleBadge() {
            // Given
            UpdateBadgeSyncUseCase.BadgeInfo badgeInfo = new UpdateBadgeSyncUseCase.BadgeInfo(
                    1L, 100, "PLATINUM"
            );
            UpdateBadgeSyncUseCase.Param param = new UpdateBadgeSyncUseCase.Param(
                    helperId, List.of(badgeInfo)
            );

            BadgeSync existingBadgeSync = mock(BadgeSync.class);
            when(badgeSyncRepository.findByHelperIdAndDisabilityCategoryId(helperId, 1L))
                    .thenReturn(Optional.of(existingBadgeSync));
            when(badgeSyncRepository.save(any(BadgeSync.class))).thenReturn(existingBadgeSync);

            // When
            UpdateBadgeSyncUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(existingBadgeSync, times(1)).updateBadge(100, "PLATINUM");
        }
    }
}
