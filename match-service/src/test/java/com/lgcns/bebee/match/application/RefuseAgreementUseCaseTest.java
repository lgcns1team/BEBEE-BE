package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.RefuseAgreementUseCase;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.vo.AgreementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("매칭 확인서 거절 유스케이스 테스트")
class RefuseAgreementUseCaseTest {

    @Mock
    private AgreementReader agreementReader;

    @InjectMocks
    private RefuseAgreementUseCase useCase;

    private Long memberId;
    private Long agreementId;
    private Long postId;
    private Long helperId;
    private Long disabledId;

    @BeforeEach
    void setUp() {
        memberId = 101L;  // 도우미
        agreementId = 999888777666L;
        postId = 1L;
        helperId = 101L;
        disabledId = 202L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("BEFORE 상태에서 거절 성공")
        void shouldRefuseAgreement_whenStatusIsBefore() throws Exception {
            // Given
            RefuseAgreementUseCase.Param param = new RefuseAgreementUseCase.Param(
                    memberId,
                    agreementId
            );

            Agreement mockAgreement = createMockAgreement(agreementId, AgreementStatus.BEFORE);

            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);

            // When
            useCase.execute(param);

            // Then
            verify(agreementReader, times(1)).getById(agreementId);
        }

        @Test
        @DisplayName("REFUSED 상태에서 다시 거절 가능")
        void shouldRefuseAgreement_whenStatusIsRefused() throws Exception {
            // Given
            RefuseAgreementUseCase.Param param = new RefuseAgreementUseCase.Param(
                    memberId,
                    agreementId
            );

            Agreement mockAgreement = createMockAgreement(agreementId, AgreementStatus.REFUSED);

            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);

            // When
            useCase.execute(param);

            // Then
            verify(agreementReader, times(1)).getById(agreementId);
        }
    }

    @Nested
    @DisplayName("파라미터 검증 실패")
    class ParameterValidationFailures {

        @Test
        @DisplayName("agreementId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenAgreementIdIsNull() {
            // Given
            RefuseAgreementUseCase.Param param = new RefuseAgreementUseCase.Param(
                    memberId,
                    null
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("agreementId");

            verify(agreementReader, never()).getById(any());
        }

        @Test
        @DisplayName("agreementId가 0 이하면 InvalidParamException 발생")
        void shouldThrowException_whenAgreementIdIsZeroOrNegative() {
            // Given
            RefuseAgreementUseCase.Param param = new RefuseAgreementUseCase.Param(
                    memberId,
                    0L
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("agreementId");
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 실패")
    class BusinessLogicFailures {

        @Test
        @DisplayName("Agreement가 존재하지 않으면 MatchException 발생")
        void shouldThrowException_whenAgreementNotFound() {
            // Given
            RefuseAgreementUseCase.Param param = new RefuseAgreementUseCase.Param(
                    memberId,
                    agreementId
            );

            when(agreementReader.getById(agreementId))
                    .thenThrow(MatchErrors.MATCH_NOT_FOUND.toException());

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);

            verify(agreementReader, times(1)).getById(agreementId);
        }

        @Test
        @DisplayName("CONFIRMED 상태에서 거절하면 MatchException 발생")
        void shouldThrowException_whenStatusIsConfirmed() throws Exception {
            // Given
            RefuseAgreementUseCase.Param param = new RefuseAgreementUseCase.Param(
                    memberId,
                    agreementId
            );

            Agreement mockAgreement = createMockAgreement(agreementId, AgreementStatus.CONFIRMED);

            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);
        }
    }

    // ========== 헬퍼 메서드 ==========

    private Agreement createMockAgreement(Long agreementId, AgreementStatus status) throws Exception {
        Agreement agreement = Agreement.create(
                postId,
                helperId,
                disabledId,
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
}