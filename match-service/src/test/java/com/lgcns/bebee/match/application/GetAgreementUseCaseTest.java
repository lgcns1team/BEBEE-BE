package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.usecase.GetAgreementUseCase;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.AgreementPeriod;
import com.lgcns.bebee.match.domain.entity.AgreementSchedule;
import com.lgcns.bebee.match.domain.entity.sync.Gender;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.service.AgreementReader;
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
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("계약 조회 유스케이스 테스트")
class GetAgreementUseCaseTest {

    @Mock
    private MemberManager memberManager;

    @Mock
    private AgreementReader agreementReader;

    @InjectMocks
    private GetAgreementUseCase useCase;

    private Long currentMemberId;
    private Long agreementId;
    private Long helperId;
    private Long disabledId;

    @BeforeEach
    void setUp() {
        currentMemberId = 101L;
        agreementId = 1L;
        helperId = 101L;
        disabledId = 102L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("HELPER가 조회하면 DISABLED 정보가 반환된다")
        void shouldReturnDisabledInfo_whenHelperQueries() {
            // Given
            GetAgreementUseCase.Param param = new GetAgreementUseCase.Param(helperId, agreementId);

            Agreement mockAgreement = mock(Agreement.class);
            AgreementPeriod mockPeriod = mock(AgreementPeriod.class);

            when(mockAgreement.getId()).thenReturn(agreementId);
            when(mockAgreement.getHelperId()).thenReturn(helperId);
            when(mockAgreement.getDisabledId()).thenReturn(disabledId);
            when(mockAgreement.getType()).thenReturn(EngagementType.DAY);
            when(mockAgreement.getPeriod()).thenReturn(mockPeriod);
            when(mockPeriod.getStartDate()).thenReturn(LocalDate.of(2025, 1, 15));
            when(mockAgreement.getSchedules()).thenReturn(List.of());
            when(mockAgreement.getUnitHoney()).thenReturn(5000L);
            when(mockAgreement.getTotalHoney()).thenReturn(5000L);

            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);

            MemberSync mockOtherMember = mock(MemberSync.class);
            when(mockOtherMember.getId()).thenReturn(disabledId);
            when(mockOtherMember.getProfileImageUrl()).thenReturn("https://example.com/profile.jpg");
            when(mockOtherMember.getNickname()).thenReturn("장애인사용자");
            when(mockOtherMember.getGender()).thenReturn(Gender.MALE);
            when(mockOtherMember.getBirthDate()).thenReturn(LocalDate.of(1990, 1, 1));

            when(memberManager.findExistingMember(disabledId)).thenReturn(mockOtherMember);

            // When
            GetAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getOtherId()).isEqualTo(disabledId);
            assertThat(result.getOtherNickname()).isEqualTo("장애인사용자");
            verify(agreementReader, times(1)).getById(agreementId);
            verify(memberManager, times(1)).findExistingMember(disabledId);
        }

        @Test
        @DisplayName("DISABLED가 조회하면 HELPER 정보가 반환된다")
        void shouldReturnHelperInfo_whenDisabledQueries() {
            // Given
            GetAgreementUseCase.Param param = new GetAgreementUseCase.Param(disabledId, agreementId);

            Agreement mockAgreement = mock(Agreement.class);
            AgreementPeriod mockPeriod = mock(AgreementPeriod.class);

            when(mockAgreement.getId()).thenReturn(agreementId);
            when(mockAgreement.getHelperId()).thenReturn(helperId);
            when(mockAgreement.getDisabledId()).thenReturn(disabledId);
            when(mockAgreement.getType()).thenReturn(EngagementType.TERM);
            when(mockAgreement.getPeriod()).thenReturn(mockPeriod);
            when(mockPeriod.getStartDate()).thenReturn(LocalDate.of(2025, 1, 1));
            when(mockPeriod.getEndDate()).thenReturn(LocalDate.of(2025, 3, 31));
            when(mockAgreement.getSchedules()).thenReturn(List.of());
            when(mockAgreement.getUnitHoney()).thenReturn(10000L);
            when(mockAgreement.getTotalHoney()).thenReturn(150000L);

            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);

            MemberSync mockOtherMember = mock(MemberSync.class);
            when(mockOtherMember.getId()).thenReturn(helperId);
            when(mockOtherMember.getProfileImageUrl()).thenReturn("https://example.com/helper.jpg");
            when(mockOtherMember.getNickname()).thenReturn("도우미사용자");
            when(mockOtherMember.getGender()).thenReturn(Gender.FEMALE);
            when(mockOtherMember.getBirthDate()).thenReturn(LocalDate.of(1985, 6, 15));

            when(memberManager.findExistingMember(helperId)).thenReturn(mockOtherMember);

            // When
            GetAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getOtherId()).isEqualTo(helperId);
            assertThat(result.getOtherNickname()).isEqualTo("도우미사용자");
            assertThat(result.getHelpType()).isEqualTo("TERM");
            assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2025, 1, 1));
            assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2025, 3, 31));
        }

        @Test
        @DisplayName("DAY 타입 계약 조회 시 date가 반환된다")
        void shouldReturnDate_whenTypeIsDay() {
            // Given
            GetAgreementUseCase.Param param = new GetAgreementUseCase.Param(helperId, agreementId);

            Agreement mockAgreement = mock(Agreement.class);
            AgreementPeriod mockPeriod = mock(AgreementPeriod.class);

            when(mockAgreement.getId()).thenReturn(agreementId);
            when(mockAgreement.getHelperId()).thenReturn(helperId);
            when(mockAgreement.getDisabledId()).thenReturn(disabledId);
            when(mockAgreement.getType()).thenReturn(EngagementType.DAY);
            when(mockAgreement.getPeriod()).thenReturn(mockPeriod);
            when(mockPeriod.getStartDate()).thenReturn(LocalDate.of(2025, 1, 15));
            when(mockAgreement.getSchedules()).thenReturn(List.of());
            when(mockAgreement.getUnitHoney()).thenReturn(5000L);
            when(mockAgreement.getTotalHoney()).thenReturn(5000L);

            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);

            MemberSync mockOtherMember = mock(MemberSync.class);
            when(mockOtherMember.getId()).thenReturn(disabledId);
            when(mockOtherMember.getProfileImageUrl()).thenReturn(null);
            when(mockOtherMember.getNickname()).thenReturn("사용자");
            when(mockOtherMember.getGender()).thenReturn(Gender.MALE);
            when(mockOtherMember.getBirthDate()).thenReturn(LocalDate.of(1990, 1, 1));

            when(memberManager.findExistingMember(disabledId)).thenReturn(mockOtherMember);

            // When
            GetAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getDate()).isEqualTo(LocalDate.of(2025, 1, 15));
            assertThat(result.getStartDate()).isNull();
            assertThat(result.getEndDate()).isNull();
        }

        @Test
        @DisplayName("스케줄 정보가 포함된 계약을 조회한다")
        void shouldReturnSchedules_whenAgreementHasSchedules() {
            // Given
            GetAgreementUseCase.Param param = new GetAgreementUseCase.Param(helperId, agreementId);

            Agreement mockAgreement = mock(Agreement.class);
            AgreementPeriod mockPeriod = mock(AgreementPeriod.class);
            AgreementSchedule mockSchedule1 = mock(AgreementSchedule.class);
            AgreementSchedule mockSchedule2 = mock(AgreementSchedule.class);

            when(mockSchedule1.getDayOfWeek()).thenReturn(DayOfWeek.MONDAY);
            when(mockSchedule1.getStartTime()).thenReturn(LocalTime.of(9, 0));
            when(mockSchedule1.getEndTime()).thenReturn(LocalTime.of(12, 0));

            when(mockSchedule2.getDayOfWeek()).thenReturn(DayOfWeek.WEDNESDAY);
            when(mockSchedule2.getStartTime()).thenReturn(LocalTime.of(14, 0));
            when(mockSchedule2.getEndTime()).thenReturn(LocalTime.of(17, 0));

            when(mockAgreement.getId()).thenReturn(agreementId);
            when(mockAgreement.getHelperId()).thenReturn(helperId);
            when(mockAgreement.getDisabledId()).thenReturn(disabledId);
            when(mockAgreement.getType()).thenReturn(EngagementType.TERM);
            when(mockAgreement.getPeriod()).thenReturn(mockPeriod);
            when(mockPeriod.getStartDate()).thenReturn(LocalDate.of(2025, 1, 1));
            when(mockPeriod.getEndDate()).thenReturn(LocalDate.of(2025, 3, 31));
            when(mockAgreement.getSchedules()).thenReturn(List.of(mockSchedule1, mockSchedule2));
            when(mockAgreement.getUnitHoney()).thenReturn(10000L);
            when(mockAgreement.getTotalHoney()).thenReturn(150000L);

            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);

            MemberSync mockOtherMember = mock(MemberSync.class);
            when(mockOtherMember.getId()).thenReturn(disabledId);
            when(mockOtherMember.getProfileImageUrl()).thenReturn(null);
            when(mockOtherMember.getNickname()).thenReturn("사용자");
            when(mockOtherMember.getGender()).thenReturn(Gender.MALE);
            when(mockOtherMember.getBirthDate()).thenReturn(LocalDate.of(1990, 1, 1));

            when(memberManager.findExistingMember(disabledId)).thenReturn(mockOtherMember);

            // When
            GetAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getSchedules()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("agreementId가 1일 때 정상 처리된다")
        void shouldWork_whenAgreementIdIsOne() {
            // Given
            GetAgreementUseCase.Param param = new GetAgreementUseCase.Param(helperId, 1L);

            Agreement mockAgreement = mock(Agreement.class);
            AgreementPeriod mockPeriod = mock(AgreementPeriod.class);

            when(mockAgreement.getId()).thenReturn(1L);
            when(mockAgreement.getHelperId()).thenReturn(helperId);
            when(mockAgreement.getDisabledId()).thenReturn(disabledId);
            when(mockAgreement.getType()).thenReturn(EngagementType.DAY);
            when(mockAgreement.getPeriod()).thenReturn(mockPeriod);
            when(mockPeriod.getStartDate()).thenReturn(LocalDate.of(2025, 1, 15));
            when(mockAgreement.getSchedules()).thenReturn(List.of());
            when(mockAgreement.getUnitHoney()).thenReturn(5000L);
            when(mockAgreement.getTotalHoney()).thenReturn(5000L);

            when(agreementReader.getById(1L)).thenReturn(mockAgreement);

            MemberSync mockOtherMember = mock(MemberSync.class);
            when(mockOtherMember.getId()).thenReturn(disabledId);
            when(mockOtherMember.getProfileImageUrl()).thenReturn(null);
            when(mockOtherMember.getNickname()).thenReturn("사용자");
            when(mockOtherMember.getGender()).thenReturn(Gender.MALE);
            when(mockOtherMember.getBirthDate()).thenReturn(LocalDate.of(1990, 1, 1));

            when(memberManager.findExistingMember(disabledId)).thenReturn(mockOtherMember);

            // When
            GetAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getAgreementId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("unitHoney가 0일 때 정상 처리된다")
        void shouldWork_whenUnitHoneyIsZero() {
            // Given
            GetAgreementUseCase.Param param = new GetAgreementUseCase.Param(helperId, agreementId);

            Agreement mockAgreement = mock(Agreement.class);
            AgreementPeriod mockPeriod = mock(AgreementPeriod.class);

            when(mockAgreement.getId()).thenReturn(agreementId);
            when(mockAgreement.getHelperId()).thenReturn(helperId);
            when(mockAgreement.getDisabledId()).thenReturn(disabledId);
            when(mockAgreement.getType()).thenReturn(EngagementType.DAY);
            when(mockAgreement.getPeriod()).thenReturn(mockPeriod);
            when(mockPeriod.getStartDate()).thenReturn(LocalDate.of(2025, 1, 15));
            when(mockAgreement.getSchedules()).thenReturn(List.of());
            when(mockAgreement.getUnitHoney()).thenReturn(0L);
            when(mockAgreement.getTotalHoney()).thenReturn(0L);

            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);

            MemberSync mockOtherMember = mock(MemberSync.class);
            when(mockOtherMember.getId()).thenReturn(disabledId);
            when(mockOtherMember.getProfileImageUrl()).thenReturn(null);
            when(mockOtherMember.getNickname()).thenReturn("사용자");
            when(mockOtherMember.getGender()).thenReturn(Gender.MALE);
            when(mockOtherMember.getBirthDate()).thenReturn(LocalDate.of(1990, 1, 1));

            when(memberManager.findExistingMember(disabledId)).thenReturn(mockOtherMember);

            // When
            GetAgreementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getUnitHoney()).isEqualTo(0L);
            assertThat(result.getTotalHoney()).isEqualTo(0L);
        }
    }
}
