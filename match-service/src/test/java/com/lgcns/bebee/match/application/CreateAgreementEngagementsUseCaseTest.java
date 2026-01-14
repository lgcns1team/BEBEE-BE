package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.usecase.CreateAgreementEngagementsUseCase;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.EngagementRepository;
import com.lgcns.bebee.match.domain.service.MatchReader;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("계약 활동 일정 생성 유스케이스 테스트")
class CreateAgreementEngagementsUseCaseTest {

    @Mock
    private MatchReader matchReader;

    @Mock
    private EngagementRepository engagementRepository;

    @InjectMocks
    private CreateAgreementEngagementsUseCase useCase;

    private Long matchId;

    @BeforeEach
    void setUp() {
        matchId = 1L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("유효한 matchId로 실행하면 Engagement가 생성된다")
        void shouldCreateEngagements_whenMatchIdIsValid() {
            // Given
            CreateAgreementEngagementsUseCase.Param param = new CreateAgreementEngagementsUseCase.Param(matchId);

            Match mockMatch = mock(Match.class);
            Agreement mockAgreement = mock(Agreement.class);

            List<LocalDate> engagementDates = List.of(
                    LocalDate.of(2025, 1, 15),
                    LocalDate.of(2025, 1, 16),
                    LocalDate.of(2025, 1, 17)
            );

            when(matchReader.getById(matchId)).thenReturn(mockMatch);
            when(mockMatch.getAgreement()).thenReturn(mockAgreement);
            when(mockAgreement.getEngagementDates()).thenReturn(engagementDates);
            when(mockAgreement.getType()).thenReturn(EngagementType.DAY);

            // When
            useCase.execute(param);

            // Then
            verify(matchReader, times(1)).getById(matchId);
            verify(engagementRepository, times(1)).saveAll(anyList());
        }

        @Test
        @DisplayName("활동 날짜 개수만큼 Engagement가 생성된다")
        @SuppressWarnings("unchecked")
        void shouldCreateEngagementsForEachDate() {
            // Given
            CreateAgreementEngagementsUseCase.Param param = new CreateAgreementEngagementsUseCase.Param(matchId);

            Match mockMatch = mock(Match.class);
            Agreement mockAgreement = mock(Agreement.class);

            List<LocalDate> engagementDates = List.of(
                    LocalDate.of(2025, 1, 15),
                    LocalDate.of(2025, 1, 16),
                    LocalDate.of(2025, 1, 17),
                    LocalDate.of(2025, 1, 18),
                    LocalDate.of(2025, 1, 19)
            );

            when(matchReader.getById(matchId)).thenReturn(mockMatch);
            when(mockMatch.getAgreement()).thenReturn(mockAgreement);
            when(mockAgreement.getEngagementDates()).thenReturn(engagementDates);
            when(mockAgreement.getType()).thenReturn(EngagementType.TERM);

            ArgumentCaptor<List<Engagement>> captor = ArgumentCaptor.forClass(List.class);

            // When
            useCase.execute(param);

            // Then
            verify(engagementRepository).saveAll(captor.capture());
            List<Engagement> savedEngagements = captor.getValue();
            assertThat(savedEngagements).hasSize(5);
        }

        @Test
        @DisplayName("빈 활동 날짜 리스트면 빈 리스트로 저장된다")
        @SuppressWarnings("unchecked")
        void shouldSaveEmptyList_whenNoEngagementDates() {
            // Given
            CreateAgreementEngagementsUseCase.Param param = new CreateAgreementEngagementsUseCase.Param(matchId);

            Match mockMatch = mock(Match.class);
            Agreement mockAgreement = mock(Agreement.class);

            List<LocalDate> emptyDates = List.of();

            when(matchReader.getById(matchId)).thenReturn(mockMatch);
            when(mockMatch.getAgreement()).thenReturn(mockAgreement);
            when(mockAgreement.getEngagementDates()).thenReturn(emptyDates);

            ArgumentCaptor<List<Engagement>> captor = ArgumentCaptor.forClass(List.class);

            // When
            useCase.execute(param);

            // Then
            verify(engagementRepository).saveAll(captor.capture());
            List<Engagement> savedEngagements = captor.getValue();
            assertThat(savedEngagements).isEmpty();
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("단일 날짜로 Engagement 1개가 생성된다")
        @SuppressWarnings("unchecked")
        void shouldCreateSingleEngagement_whenOneDateProvided() {
            // Given
            CreateAgreementEngagementsUseCase.Param param = new CreateAgreementEngagementsUseCase.Param(matchId);

            Match mockMatch = mock(Match.class);
            Agreement mockAgreement = mock(Agreement.class);

            List<LocalDate> singleDate = List.of(LocalDate.of(2025, 1, 15));

            when(matchReader.getById(matchId)).thenReturn(mockMatch);
            when(mockMatch.getAgreement()).thenReturn(mockAgreement);
            when(mockAgreement.getEngagementDates()).thenReturn(singleDate);
            when(mockAgreement.getType()).thenReturn(EngagementType.DAY);

            ArgumentCaptor<List<Engagement>> captor = ArgumentCaptor.forClass(List.class);

            // When
            useCase.execute(param);

            // Then
            verify(engagementRepository).saveAll(captor.capture());
            List<Engagement> savedEngagements = captor.getValue();
            assertThat(savedEngagements).hasSize(1);
        }

        @Test
        @DisplayName("matchId가 1일 때 정상 처리된다")
        void shouldWork_whenMatchIdIsOne() {
            // Given
            CreateAgreementEngagementsUseCase.Param param = new CreateAgreementEngagementsUseCase.Param(1L);

            Match mockMatch = mock(Match.class);
            Agreement mockAgreement = mock(Agreement.class);

            when(matchReader.getById(1L)).thenReturn(mockMatch);
            when(mockMatch.getAgreement()).thenReturn(mockAgreement);
            when(mockAgreement.getEngagementDates()).thenReturn(List.of(LocalDate.now()));
            when(mockAgreement.getType()).thenReturn(EngagementType.DAY);

            // When
            useCase.execute(param);

            // Then
            verify(matchReader, times(1)).getById(1L);
            verify(engagementRepository, times(1)).saveAll(anyList());
        }
    }
}
