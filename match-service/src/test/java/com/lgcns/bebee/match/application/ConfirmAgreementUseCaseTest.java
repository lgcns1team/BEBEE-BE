package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.ConfirmAgreementUseCase;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.vo.AgreementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import com.lgcns.bebee.match.domain.service.AgreementReader;
import com.lgcns.bebee.match.exception.MatchErrors;
import com.lgcns.bebee.match.exception.MatchException;
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

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("매칭 확인서 수락 유스케이스 테스트")
class ConfirmAgreementUseCaseTest {

    @Mock
    private AgreementReader agreementReader;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private ConfirmAgreementUseCase useCase;

    private Long agreementId;
    private Long matchId;
    private Long helperId;
    private Long disabledId;
    private Long postId;
    private String title;
    private Long chatRoomId;

    @BeforeEach
    void setUp() {
        agreementId = 999888777666L;
        matchId = 100L;
        helperId = 101L;
        disabledId = 202L;
        postId = 1L;
        title = "도움이 필요합니다";
        chatRoomId = 303L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("BEFORE 상태에서 수락 성공")
        void shouldConfirmAgreement_whenStatusIsBefore() throws Exception {
            // Given
            ConfirmAgreementUseCase.Param param = createValidParam();

            Agreement mockAgreement = createMockAgreement(agreementId, AgreementStatus.BEFORE);
            Match savedMatch = createMockMatch(helperId, disabledId);

            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);
            when(matchRepository.save(any(Match.class))).thenReturn(savedMatch);

            // When
            ConfirmAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMatchId()).isNotNull();
            verify(agreementReader, times(1)).getById(agreementId);
            verify(matchRepository, times(1)).save(any(Match.class));
        }

        @Test
        @DisplayName("REFUSED 상태에서 수락 성공")
        void shouldConfirmAgreement_whenStatusIsRefused() throws Exception {
            // Given
            ConfirmAgreementUseCase.Param param = createValidParam();

            Agreement mockAgreement = createMockAgreement(agreementId, AgreementStatus.REFUSED);
            Match savedMatch = createMockMatch(helperId, disabledId);

            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);
            when(matchRepository.save(any(Match.class))).thenReturn(savedMatch);

            // When
            ConfirmAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(matchRepository, times(1)).save(any(Match.class));
        }
    }

    @Nested
    @DisplayName("파라미터 검증 실패")
    class ParameterValidationFailures {

        @Test
        @DisplayName("agreementId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenAgreementIdIsNull() {
            // Given
            ConfirmAgreementUseCase.Param param = new ConfirmAgreementUseCase.Param(
                    helperId,
                    disabledId,
                    postId,
                    title,
                    chatRoomId,
                    null
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("agreementId");
        }

        @Test
        @DisplayName("helperId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenHelperIdIsNull() {
            // Given
            ConfirmAgreementUseCase.Param param = new ConfirmAgreementUseCase.Param(
                    null,
                    disabledId,
                    postId,
                    title,
                    chatRoomId,
                    agreementId
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("helperId");
        }

        @Test
        @DisplayName("disabledId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenDisabledIdIsNull() {
            // Given
            ConfirmAgreementUseCase.Param param = new ConfirmAgreementUseCase.Param(
                    helperId,
                    null,
                    postId,
                    title,
                    chatRoomId,
                    agreementId
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("disabledId");
        }

        @Test
        @DisplayName("postId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenPostIdIsNull() {
            // Given
            ConfirmAgreementUseCase.Param param = new ConfirmAgreementUseCase.Param(
                    helperId,
                    disabledId,
                    null,
                    title,
                    chatRoomId,
                    agreementId
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("postId");
        }

        @Test
        @DisplayName("title이 null이면 InvalidParamException 발생")
        void shouldThrowException_whenTitleIsNull() {
            // Given
            ConfirmAgreementUseCase.Param param = new ConfirmAgreementUseCase.Param(
                    helperId,
                    disabledId,
                    postId,
                    null,
                    chatRoomId,
                    agreementId
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("title");
        }

        @Test
        @DisplayName("title이 빈 문자열이면 InvalidParamException 발생")
        void shouldThrowException_whenTitleIsEmpty() {
            // Given
            ConfirmAgreementUseCase.Param param = new ConfirmAgreementUseCase.Param(
                    helperId,
                    disabledId,
                    postId,
                    "",
                    chatRoomId,
                    agreementId
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("title");
        }

        @Test
        @DisplayName("title이 100자를 초과하면 InvalidParamException 발생")
        void shouldThrowException_whenTitleExceeds100Characters() {
            // Given
            String longTitle = "a".repeat(101);
            ConfirmAgreementUseCase.Param param = new ConfirmAgreementUseCase.Param(
                    helperId,
                    disabledId,
                    postId,
                    longTitle,
                    chatRoomId,
                    agreementId
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("title");
        }

        @Test
        @DisplayName("chatRoomId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenChatRoomIdIsNull() {
            // Given
            ConfirmAgreementUseCase.Param param = new ConfirmAgreementUseCase.Param(
                    helperId,
                    disabledId,
                    postId,
                    title,
                    null,
                    agreementId
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("chatRoomId");
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 실패")
    class BusinessLogicFailures {

        @Test
        @DisplayName("Agreement가 존재하지 않으면 MatchException 발생")
        void shouldThrowException_whenAgreementNotFound() {
            // Given
            ConfirmAgreementUseCase.Param param = createValidParam();

            when(agreementReader.getById(agreementId))
                    .thenThrow(MatchErrors.MATCH_NOT_FOUND.toException());

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);

            verify(agreementReader, times(1)).getById(agreementId);
            verify(matchRepository, never()).save(any());
        }

        @Test
        @DisplayName("CONFIRMED 상태에서 다시 수락하면 MatchException 발생")
        void shouldThrowException_whenStatusIsConfirmed() throws Exception {
            // Given
            ConfirmAgreementUseCase.Param param = createValidParam();

            Agreement mockAgreement = createMockAgreement(agreementId, AgreementStatus.CONFIRMED);

            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);

            verify(matchRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("title이 정확히 100자일 때 정상 처리")
        void shouldConfirmAgreement_whenTitleIs100Characters() throws Exception {
            // Given
            String titleWith100Chars = "a".repeat(100);
            ConfirmAgreementUseCase.Param param = new ConfirmAgreementUseCase.Param(
                    helperId,
                    disabledId,
                    postId,
                    titleWith100Chars,
                    chatRoomId,
                    agreementId
            );

            Agreement mockAgreement = createMockAgreement(agreementId, AgreementStatus.BEFORE);
            Match savedMatch = createMockMatch(helperId, disabledId);

            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);
            when(matchRepository.save(any(Match.class))).thenReturn(savedMatch);

            // When
            ConfirmAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(matchRepository, times(1)).save(any(Match.class));
        }
    }

    // ========== 헬퍼 메서드 ==========

    private ConfirmAgreementUseCase.Param createValidParam() {
        return new ConfirmAgreementUseCase.Param(
                helperId,
                disabledId,
                postId,
                title,
                chatRoomId,
                agreementId
        );
    }

    private Agreement createMockAgreement(Long agreementId, AgreementStatus status) throws Exception {
        Agreement agreement = Agreement.create(
                EngagementType.DAY,
                false,
                200,
                200,
                "서울특별시 강동구",
                List.of(1L, 2L)
        );

        // Reflection으로 ID 설정
        Field idField = Agreement.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(agreement, agreementId);

        // Reflection으로 status 설정
        Field statusField = Agreement.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(agreement, status);

        return agreement;
    }

    private Match createMockMatch(Long helperId, Long disabledId) {
        Match match = mock(Match.class);

        when(match.getMatchId()).thenReturn(matchId);
        when(match.getHelperId()).thenReturn(helperId);
        when(match.getDisabledId()).thenReturn(disabledId);

        return match;
    }
}