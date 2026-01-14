package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.data.event.DomainEventPublisher;
import com.lgcns.bebee.common.data.event.match.ReviewCreatedEvent;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.ReviewManager;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("리뷰 생성 유스케이스 테스트")
class CreateReviewUseCaseTest {

    @Mock
    private MemberManager memberManager;

    @Mock
    private ReviewManager reviewManager;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private CreateReviewUseCase useCase;

    private Long matchId;
    private Long reviewerId;
    private List<Integer> keywordIds;

    @BeforeEach
    void setUp() {
        matchId = 1L;
        reviewerId = 101L;
        keywordIds = List.of(1, 2, 3);
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("DISABLED 회원이 리뷰를 작성하면 DISABLED_TO_HELPER 방향으로 생성된다")
        void shouldCreateReview_whenDisabledMemberReviews() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(matchId, reviewerId, keywordIds);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(reviewerId)).thenReturn(mockMember);

            when(reviewManager.determineReviewDirection(mockMember)).thenReturn(ReviewDirection.DISABLED_TO_HELPER);
            doNothing().when(reviewManager).validateKeywords(anyList(), any(ReviewDirection.class));

            Review mockReview = mock(Review.class);
            Match mockMatch = mock(Match.class);
            when(mockReview.getId()).thenReturn(1L);
            when(mockReview.getMatch()).thenReturn(mockMatch);
            when(mockMatch.getMatchId()).thenReturn(matchId);
            when(mockReview.getReviewerId()).thenReturn(reviewerId);
            when(mockReview.getRevieweeId()).thenReturn(102L);
            when(mockReview.getReviewDirection()).thenReturn(ReviewDirection.DISABLED_TO_HELPER);

            when(reviewManager.createReview(anyLong(), anyLong(), any(ReviewDirection.class), anyList()))
                    .thenReturn(mockReview);

            // When
            CreateReviewUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMessage()).contains("리뷰가 작성되었습니다");
            verify(reviewManager, times(1)).determineReviewDirection(mockMember);
            verify(reviewManager, times(1)).validateKeywords(keywordIds, ReviewDirection.DISABLED_TO_HELPER);
            verify(reviewManager, times(1)).createReview(matchId, reviewerId, ReviewDirection.DISABLED_TO_HELPER, keywordIds);
            verify(eventPublisher, times(1)).publish(any(ReviewCreatedEvent.class));
        }

        @Test
        @DisplayName("HELPER 회원이 리뷰를 작성하면 HELPER_TO_DISABLED 방향으로 생성된다")
        void shouldCreateReview_whenHelperMemberReviews() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(matchId, reviewerId, keywordIds);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(reviewerId)).thenReturn(mockMember);

            when(reviewManager.determineReviewDirection(mockMember)).thenReturn(ReviewDirection.HELPER_TO_DISABLED);
            doNothing().when(reviewManager).validateKeywords(anyList(), any(ReviewDirection.class));

            Review mockReview = mock(Review.class);
            Match mockMatch = mock(Match.class);
            when(mockReview.getId()).thenReturn(2L);
            when(mockReview.getMatch()).thenReturn(mockMatch);
            when(mockMatch.getMatchId()).thenReturn(matchId);
            when(mockReview.getReviewerId()).thenReturn(reviewerId);
            when(mockReview.getRevieweeId()).thenReturn(103L);
            when(mockReview.getReviewDirection()).thenReturn(ReviewDirection.HELPER_TO_DISABLED);

            when(reviewManager.createReview(anyLong(), anyLong(), any(ReviewDirection.class), anyList()))
                    .thenReturn(mockReview);

            // When
            CreateReviewUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(reviewManager, times(1)).createReview(matchId, reviewerId, ReviewDirection.HELPER_TO_DISABLED, keywordIds);
        }
    }

    @Nested
    @DisplayName("파라미터 검증 실패")
    class ParameterValidationFailures {

        @Test
        @DisplayName("matchId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenMatchIdIsNull() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(null, reviewerId, keywordIds);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("matchId");
        }

        @Test
        @DisplayName("matchId가 0 이하면 InvalidParamException 발생")
        void shouldThrowException_whenMatchIdIsZeroOrNegative() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(0L, reviewerId, keywordIds);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("matchId");
        }

        @Test
        @DisplayName("reviewerId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenReviewerIdIsNull() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(matchId, null, keywordIds);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("reviewerId");
        }

        @Test
        @DisplayName("reviewerId가 0 이하면 InvalidParamException 발생")
        void shouldThrowException_whenReviewerIdIsZeroOrNegative() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(matchId, -1L, keywordIds);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("reviewerId");
        }

        @Test
        @DisplayName("keywordIds가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenKeywordIdsIsNull() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(matchId, reviewerId, null);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("keywordIds");
        }

        @Test
        @DisplayName("keywordIds가 빈 리스트면 InvalidParamException 발생")
        void shouldThrowException_whenKeywordIdsIsEmpty() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(matchId, reviewerId, List.of());

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("keywordIds");
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("matchId가 1일 때 정상 처리된다")
        void shouldCreateReview_whenMatchIdIsOne() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(1L, reviewerId, keywordIds);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(reviewerId)).thenReturn(mockMember);

            when(reviewManager.determineReviewDirection(mockMember)).thenReturn(ReviewDirection.DISABLED_TO_HELPER);
            doNothing().when(reviewManager).validateKeywords(anyList(), any(ReviewDirection.class));

            Review mockReview = mock(Review.class);
            Match mockMatch = mock(Match.class);
            when(mockReview.getId()).thenReturn(1L);
            when(mockReview.getMatch()).thenReturn(mockMatch);
            when(mockMatch.getMatchId()).thenReturn(1L);
            when(mockReview.getReviewerId()).thenReturn(reviewerId);
            when(mockReview.getRevieweeId()).thenReturn(102L);
            when(mockReview.getReviewDirection()).thenReturn(ReviewDirection.DISABLED_TO_HELPER);

            when(reviewManager.createReview(anyLong(), anyLong(), any(ReviewDirection.class), anyList()))
                    .thenReturn(mockReview);

            // When
            CreateReviewUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(reviewManager, times(1)).createReview(eq(1L), eq(reviewerId), any(ReviewDirection.class), eq(keywordIds));
        }

        @Test
        @DisplayName("keywordIds가 1개일 때 정상 처리된다")
        void shouldCreateReview_whenSingleKeyword() {
            // Given
            List<Integer> singleKeyword = List.of(1);
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(matchId, reviewerId, singleKeyword);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(reviewerId)).thenReturn(mockMember);

            when(reviewManager.determineReviewDirection(mockMember)).thenReturn(ReviewDirection.HELPER_TO_DISABLED);
            doNothing().when(reviewManager).validateKeywords(anyList(), any(ReviewDirection.class));

            Review mockReview = mock(Review.class);
            Match mockMatch = mock(Match.class);
            when(mockReview.getId()).thenReturn(1L);
            when(mockReview.getMatch()).thenReturn(mockMatch);
            when(mockMatch.getMatchId()).thenReturn(matchId);
            when(mockReview.getReviewerId()).thenReturn(reviewerId);
            when(mockReview.getRevieweeId()).thenReturn(102L);
            when(mockReview.getReviewDirection()).thenReturn(ReviewDirection.HELPER_TO_DISABLED);

            when(reviewManager.createReview(anyLong(), anyLong(), any(ReviewDirection.class), anyList()))
                    .thenReturn(mockReview);

            // When
            CreateReviewUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(reviewManager, times(1)).createReview(matchId, reviewerId, ReviewDirection.HELPER_TO_DISABLED, singleKeyword);
        }
    }
}
