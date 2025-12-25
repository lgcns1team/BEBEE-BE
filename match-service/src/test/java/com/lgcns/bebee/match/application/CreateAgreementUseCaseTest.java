package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.CreateAgreementUseCase;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.vo.AgreementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.AgreementRepository;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.common.exception.MatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("매칭 확인서 생성 유스케이스 테스트")
class CreateAgreementUseCaseTest {

    @Mock
    private AgreementRepository agreementRepository;

    @InjectMocks
    private CreateAgreementUseCase useCase;

    private Long memberId;

    @BeforeEach
    void setUp() {
        memberId = 1L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("모든 파라미터가 유효하면 Agreement가 생성된다")
        void shouldCreateAgreement_whenAllParametersAreValid() throws Exception {
            // Given
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    EngagementType.DAY,
                    false,
                    5000,
                    5000,
                    "서울특별시 중구",
                    List.of(1L, 2L)
            );

            Agreement savedAgreement = createMockAgreement(1L, 5000, 5000);

            when(agreementRepository.save(any(Agreement.class))).thenReturn(savedAgreement);

            // When
            CreateAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getUnitHoney()).isEqualTo(5000);
            assertThat(result.getTotalHoney()).isEqualTo(5000);
            assertThat(result.getStatus()).isEqualTo(AgreementStatus.BEFORE);

            // Mock 호출 검증
            verify(agreementRepository, times(1)).save(any(Agreement.class));
        }

        @Test
        @DisplayName("TERM 타입으로도 Agreement가 생성된다")
        void shouldCreateAgreement_whenTypeIsTerm() throws Exception {
            // Given
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    EngagementType.TERM,
                    true,
                    10000,
                    150000,
                    "경기도 성남시",
                    List.of(3L, 4L)
            );

            Agreement savedAgreement = createMockAgreementWithType(2L, EngagementType.TERM, 10000, 150000, true);

            when(agreementRepository.save(any(Agreement.class))).thenReturn(savedAgreement);

            // When
            CreateAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getType()).isEqualTo(EngagementType.TERM);
            assertThat(result.getIsVolunteer()).isTrue();

            verify(agreementRepository, times(1)).save(any(Agreement.class));
        }

        @Test
        @DisplayName("isVolunteer가 true면 unitHoney, totalHoney가 무조건 0으로 생성된다")
        void shouldHoneyFree_whenIsVolunteerIsTrue() throws Exception {
            // Given
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    EngagementType.DAY,
                    true,
                    500,
                    500,
                    "서울특별시 중구",
                    List.of(3L)
            );

            when(agreementRepository.save(any(Agreement.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            CreateAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getUnitHoney()).isEqualTo(0);
            assertThat(result.getTotalHoney()).isEqualTo(0);
            assertThat(result.getStatus()).isEqualTo(AgreementStatus.BEFORE);

            // Mock 호출 검증
            verify(agreementRepository, times(1)).save(any(Agreement.class));
        }
    }

    @Nested
    @DisplayName("파라미터 검증 실패")
    class ParameterValidationFailures {

        @Test
        @DisplayName("unitHoney가 음수면 InvalidParamException 발생")
        void shouldThrowException_whenUnitHoneyIsNegative() {
            // Given
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    EngagementType.DAY,
                    false,
                    -1000, // 음수
                    5000,
                    "서울특별시",
                    List.of(1L, 2L)
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("unitHoney");
        }

        @Test
        @DisplayName("totalHoney가 음수면 InvalidParamException 발생")
        void shouldThrowException_whenTotalHoneyIsNegative() {
            // Given
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    EngagementType.DAY,
                    false,
                    5000,
                    -1000, // 음수
                    "서울특별시",
                    List.of(1L, 2L)
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("totalHoney");
        }

        @Test
        @DisplayName("region이 null이면 InvalidParamException 발생")
        void shouldThrowException_whenRegionIsNull() {
            // Given
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    EngagementType.DAY,
                    false,
                    5000,
                    5000,
                    null, // region null
                    List.of(1L, 2L)
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("region");
        }

        @Test
        @DisplayName("region이 빈 문자열이면 InvalidParamException 발생")
        void shouldThrowException_whenRegionIsEmpty() {
            // Given
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    EngagementType.DAY,
                    false,
                    5000,
                    5000,
                    "", // 빈 문자열
                    List.of(1L, 2L)
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("region");
        }

        @Test
        @DisplayName("region이 50자를 초과하면 InvalidParamException 발생")
        void shouldThrowException_whenRegionExceeds50Characters() {
            // Given
            String longRegion = "a".repeat(51); // 51자
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    EngagementType.DAY,
                    false,
                    5000,
                    5000,
                    longRegion,
                    List.of(1L, 2L)
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("region");
        }

        @Test
        @DisplayName("type이 null이면 InvalidParamException 발생")
        void shouldThrowException_whenTypeIsNull() {
            // Given
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    null, // type null
                    false,
                    5000,
                    5000,
                    "서울특별시",
                    List.of(1L, 2L)
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("type");
        }

        @Test
        @DisplayName("helpCategoryIds가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenHelpCategoryIdsIsNull() {
            // Given
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    EngagementType.DAY,
                    false,
                    5000,
                    5000,
                    "서울특별시",
                    null // helpCategoryIds null
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("helpCategoryIds");
        }

        @Test
        @DisplayName("helpCategoryIds가 빈 리스트면 InvalidParamException 발생")
        void shouldThrowException_whenHelpCategoryIdsIsEmpty() {
            // Given
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    EngagementType.DAY,
                    false,
                    5000,
                    5000,
                    "서울특별시",
                    List.of()
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("helpCategoryIds");
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("region이 정확히 50자일 때 정상 처리")
        void shouldCreateAgreement_whenRegionIs50Characters() throws Exception {
            // Given
            String regionWith50Chars = "a".repeat(50); // 정확히 50자
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    EngagementType.DAY,
                    false,
                    5000,
                    5000,
                    regionWith50Chars,
                    List.of(1L, 2L)
            );

            Agreement savedAgreement = createMockAgreement(1L, 5000, 5000);

            when(agreementRepository.save(any(Agreement.class))).thenReturn(savedAgreement);

            // When
            CreateAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(agreementRepository, times(1)).save(any(Agreement.class));
        }

        @Test
        @DisplayName("unitHoney가 0일 때 정상 처리")
        void shouldCreateAgreement_whenUnitHoneyIsZero() throws Exception {
            // Given
            CreateAgreementUseCase.Param param = new CreateAgreementUseCase.Param(
                    memberId,
                    EngagementType.DAY,
                    false,
                    0, // 0
                    0,
                    "서울특별시",
                    List.of(1L, 2L)
            );

            Agreement savedAgreement = createMockAgreement(1L, 0, 0);

            when(agreementRepository.save(any(Agreement.class))).thenReturn(savedAgreement);

            // When
            CreateAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getUnitHoney()).isEqualTo(0);
        }
    }

    // ========== 헬퍼 메서드 ==========

    private CreateAgreementUseCase.Param createValidParam() {
        return new CreateAgreementUseCase.Param(
                memberId,
                EngagementType.DAY,
                false,
                5000,
                5000,
                "서울특별시",
                List.of(1L, 2L)
        );
    }

    /**
     * Mock Agreement 생성 (DAY 타입)
     */
    private Agreement createMockAgreement(Long agreementId, Integer unitHoney, Integer totalHoney) throws Exception {
        Agreement agreement = Agreement.create(
                EngagementType.DAY,
                false,
                unitHoney,
                totalHoney,
                "서울특별시",
                List.of(1L, 2L)
        );

        // Reflection으로 ID 설정
        Field idField = Agreement.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(agreement, agreementId);

        return agreement;
    }

    /**
     * Mock Agreement 생성 (타입 지정 가능)
     */
    private Agreement createMockAgreementWithType(
            Long agreementId,
            EngagementType type,
            Integer unitHoney,
            Integer totalHoney,
            Boolean isVolunteer
    ) throws Exception {
        Agreement agreement = Agreement.create(
                type,
                isVolunteer,
                unitHoney,
                totalHoney,
                "서울특별시",
                List.of(1L, 2L)
        );

        // Reflection으로 ID 설정
        Field idField = Agreement.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(agreement, agreementId);

        return agreement;
    }
}