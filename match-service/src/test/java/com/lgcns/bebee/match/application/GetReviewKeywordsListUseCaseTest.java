package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.GetReviewKeywordsListUseCase;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("리뷰 키워드 목록 조회 유스케이스 테스트")
class GetReviewKeywordsListUseCaseTest {

    @Mock
    private MemberManager memberManager;

    @Mock
    private ReviewManager reviewManager;

    @InjectMocks
    private GetReviewKeywordsListUseCase useCase;

    private Long memberId;

    @BeforeEach
    void setUp() {
        memberId = 101L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("DISABLED 회원은 DISABLED_TO_HELPER 방향의 키워드를 받는다")
        void shouldReturnDisabledToHelperKeywords_whenMemberIsDisabled() {
            // Given
            GetReviewKeywordsListUseCase.Param param = new GetReviewKeywordsListUseCase.Param(memberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            when(reviewManager.determineReviewDirection(mockMember))
                    .thenReturn(ReviewDirection.DISABLED_TO_HELPER);

            // When
            GetReviewKeywordsListUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getKeywords()).isNotEmpty();
            verify(memberManager, times(1)).findExistingMember(memberId);
            verify(reviewManager, times(1)).determineReviewDirection(mockMember);
        }

        @Test
        @DisplayName("HELPER 회원은 HELPER_TO_DISABLED 방향의 키워드를 받는다")
        void shouldReturnHelperToDisabledKeywords_whenMemberIsHelper() {
            // Given
            GetReviewKeywordsListUseCase.Param param = new GetReviewKeywordsListUseCase.Param(memberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            when(reviewManager.determineReviewDirection(mockMember))
                    .thenReturn(ReviewDirection.HELPER_TO_DISABLED);

            // When
            GetReviewKeywordsListUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getKeywords()).isNotEmpty();
            verify(reviewManager, times(1)).determineReviewDirection(mockMember);
        }

        @Test
        @DisplayName("키워드에는 긍정과 부정 키워드가 모두 포함된다")
        void shouldReturnBothPositiveAndNegativeKeywords() {
            // Given
            GetReviewKeywordsListUseCase.Param param = new GetReviewKeywordsListUseCase.Param(memberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            when(reviewManager.determineReviewDirection(mockMember))
                    .thenReturn(ReviewDirection.DISABLED_TO_HELPER);

            // When
            GetReviewKeywordsListUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getKeywords()).isNotEmpty();

            boolean hasPositive = result.getKeywords().stream()
                    .anyMatch(GetReviewKeywordsListUseCase.KeywordDTO::getIsPositive);
            boolean hasNegative = result.getKeywords().stream()
                    .anyMatch(k -> !k.getIsPositive());

            assertThat(hasPositive).isTrue();
            assertThat(hasNegative).isTrue();
        }

        @Test
        @DisplayName("각 키워드에는 ID, 설명, 긍정/부정 여부가 포함된다")
        void shouldReturnKeywordsWithAllFields() {
            // Given
            GetReviewKeywordsListUseCase.Param param = new GetReviewKeywordsListUseCase.Param(memberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            when(reviewManager.determineReviewDirection(mockMember))
                    .thenReturn(ReviewDirection.HELPER_TO_DISABLED);

            // When
            GetReviewKeywordsListUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getKeywords()).isNotEmpty();
            GetReviewKeywordsListUseCase.KeywordDTO firstKeyword = result.getKeywords().get(0);

            assertThat(firstKeyword.getKeywordId()).isNotNull();
            assertThat(firstKeyword.getDescription()).isNotEmpty();
            assertThat(firstKeyword.getIsPositive()).isNotNull();
        }
    }

    @Nested
    @DisplayName("파라미터 검증 실패")
    class ParameterValidationFailures {

        @Test
        @DisplayName("memberId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenMemberIdIsNull() {
            // Given
            GetReviewKeywordsListUseCase.Param param = new GetReviewKeywordsListUseCase.Param(null);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("memberId");
        }

        @Test
        @DisplayName("memberId가 0 이하면 InvalidParamException 발생")
        void shouldThrowException_whenMemberIdIsZeroOrNegative() {
            // Given
            GetReviewKeywordsListUseCase.Param param = new GetReviewKeywordsListUseCase.Param(0L);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("memberId");
        }

        @Test
        @DisplayName("memberId가 음수이면 InvalidParamException 발생")
        void shouldThrowException_whenMemberIdIsNegative() {
            // Given
            GetReviewKeywordsListUseCase.Param param = new GetReviewKeywordsListUseCase.Param(-1L);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("memberId");
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("memberId가 1일 때 정상 처리된다")
        void shouldWork_whenMemberIdIsOne() {
            // Given
            GetReviewKeywordsListUseCase.Param param = new GetReviewKeywordsListUseCase.Param(1L);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(1L)).thenReturn(mockMember);

            when(reviewManager.determineReviewDirection(mockMember))
                    .thenReturn(ReviewDirection.DISABLED_TO_HELPER);

            // When
            GetReviewKeywordsListUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getKeywords()).isNotEmpty();
            verify(memberManager, times(1)).findExistingMember(1L);
        }
    }
}
