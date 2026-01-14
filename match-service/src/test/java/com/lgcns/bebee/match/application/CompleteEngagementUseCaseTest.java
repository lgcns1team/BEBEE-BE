package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.data.event.DomainEventPublisher;
import com.lgcns.bebee.common.data.event.match.EngagementCompletedEvent;
import com.lgcns.bebee.match.application.usecase.CompleteEngagementUseCase;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.AgreementPeriod;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;
import com.lgcns.bebee.match.domain.service.EngagementManager;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("활동 완료 유스케이스 테스트")
class CompleteEngagementUseCaseTest {

    @Mock
    private MemberManager memberManager;

    @Mock
    private EngagementManager engagementManager;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private CompleteEngagementUseCase useCase;

    private Long memberId;
    private Long engagementId;

    @BeforeEach
    void setUp() {
        memberId = 101L;
        engagementId = 1001L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("HELPER가 활동 완료 처리하면 checkHelper가 호출된다")
        void shouldCallCheckHelper_whenMemberIsHelper() {
            // Given
            CompleteEngagementUseCase.Param param = new CompleteEngagementUseCase.Param(memberId, engagementId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Engagement mockEngagement = mock(Engagement.class);
            Match mockMatch = mock(Match.class);
            Agreement mockAgreement = mock(Agreement.class);
            AgreementPeriod mockPeriod = mock(AgreementPeriod.class);

            when(engagementManager.findExistingEngagement(engagementId)).thenReturn(mockEngagement);
            when(mockEngagement.getStatus()).thenReturn(EngagementStatus.NOT_COMPLETED);
            when(mockEngagement.getMatch()).thenReturn(mockMatch);
            when(mockMatch.getAgreement()).thenReturn(mockAgreement);
            when(mockAgreement.getPeriod()).thenReturn(mockPeriod);
            when(mockPeriod.getEndDate()).thenReturn(LocalDate.of(2025, 12, 31));
            when(mockEngagement.getDate()).thenReturn(LocalDate.of(2025, 1, 15));

            // When
            CompleteEngagementUseCase.Result result = useCase.execute(param);

            // Then
            verify(mockEngagement, times(1)).checkHelper();
            verify(mockEngagement, never()).checkDisabled();
            assertThat(result.getIsLastEngagement()).isFalse();
        }

        @Test
        @DisplayName("DISABLED가 활동 완료 처리하면 checkDisabled가 호출된다")
        void shouldCallCheckDisabled_whenMemberIsDisabled() {
            // Given
            CompleteEngagementUseCase.Param param = new CompleteEngagementUseCase.Param(memberId, engagementId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Engagement mockEngagement = mock(Engagement.class);
            Match mockMatch = mock(Match.class);
            Agreement mockAgreement = mock(Agreement.class);
            AgreementPeriod mockPeriod = mock(AgreementPeriod.class);

            when(engagementManager.findExistingEngagement(engagementId)).thenReturn(mockEngagement);
            when(mockEngagement.getStatus()).thenReturn(EngagementStatus.NOT_COMPLETED);
            when(mockEngagement.getMatch()).thenReturn(mockMatch);
            when(mockMatch.getAgreement()).thenReturn(mockAgreement);
            when(mockAgreement.getPeriod()).thenReturn(mockPeriod);
            when(mockPeriod.getEndDate()).thenReturn(LocalDate.of(2025, 12, 31));
            when(mockEngagement.getDate()).thenReturn(LocalDate.of(2025, 1, 15));

            // When
            CompleteEngagementUseCase.Result result = useCase.execute(param);

            // Then
            verify(mockEngagement, times(1)).checkDisabled();
            verify(mockEngagement, never()).checkHelper();
        }

        @Test
        @DisplayName("활동이 COMPLETED 상태가 되면 이벤트가 발행된다")
        void shouldPublishEvent_whenEngagementIsCompleted() {
            // Given
            CompleteEngagementUseCase.Param param = new CompleteEngagementUseCase.Param(memberId, engagementId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Engagement mockEngagement = mock(Engagement.class);
            Match mockMatch = mock(Match.class);
            Agreement mockAgreement = mock(Agreement.class);
            AgreementPeriod mockPeriod = mock(AgreementPeriod.class);

            when(engagementManager.findExistingEngagement(engagementId)).thenReturn(mockEngagement);
            when(mockEngagement.getStatus()).thenReturn(EngagementStatus.COMPLETED);
            when(mockEngagement.getId()).thenReturn(engagementId);
            when(mockEngagement.getDate()).thenReturn(LocalDate.of(2025, 1, 15));
            when(mockEngagement.getMatch()).thenReturn(mockMatch);
            when(mockMatch.getAgreementId()).thenReturn(1L);
            when(mockMatch.getHelperId()).thenReturn(2L);
            when(mockMatch.getDisabledId()).thenReturn(3L);
            when(mockMatch.getAgreement()).thenReturn(mockAgreement);
            when(mockAgreement.getPeriod()).thenReturn(mockPeriod);
            when(mockPeriod.getEndDate()).thenReturn(LocalDate.of(2025, 12, 31));

            // When
            useCase.execute(param);

            // Then
            verify(eventPublisher, times(1)).publish(any(EngagementCompletedEvent.class));
        }

        @Test
        @DisplayName("마지막 활동일이면 isLastEngagement가 true를 반환한다")
        void shouldReturnTrue_whenIsLastEngagement() {
            // Given
            LocalDate lastDate = LocalDate.of(2025, 1, 15);
            CompleteEngagementUseCase.Param param = new CompleteEngagementUseCase.Param(memberId, engagementId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Engagement mockEngagement = mock(Engagement.class);
            Match mockMatch = mock(Match.class);
            Agreement mockAgreement = mock(Agreement.class);
            AgreementPeriod mockPeriod = mock(AgreementPeriod.class);

            when(engagementManager.findExistingEngagement(engagementId)).thenReturn(mockEngagement);
            when(mockEngagement.getStatus()).thenReturn(EngagementStatus.NOT_COMPLETED);
            when(mockEngagement.getMatch()).thenReturn(mockMatch);
            when(mockMatch.getAgreement()).thenReturn(mockAgreement);
            when(mockAgreement.getPeriod()).thenReturn(mockPeriod);
            when(mockPeriod.getEndDate()).thenReturn(lastDate);
            when(mockEngagement.getDate()).thenReturn(lastDate);

            // When
            CompleteEngagementUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getIsLastEngagement()).isTrue();
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 검증")
    class BusinessLogicTests {

        @Test
        @DisplayName("활동이 COMPLETED가 아니면 이벤트가 발행되지 않는다")
        void shouldNotPublishEvent_whenEngagementIsNotCompleted() {
            // Given
            CompleteEngagementUseCase.Param param = new CompleteEngagementUseCase.Param(memberId, engagementId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Engagement mockEngagement = mock(Engagement.class);
            Match mockMatch = mock(Match.class);
            Agreement mockAgreement = mock(Agreement.class);
            AgreementPeriod mockPeriod = mock(AgreementPeriod.class);

            when(engagementManager.findExistingEngagement(engagementId)).thenReturn(mockEngagement);
            when(mockEngagement.getStatus()).thenReturn(EngagementStatus.NOT_COMPLETED);
            when(mockEngagement.getMatch()).thenReturn(mockMatch);
            when(mockMatch.getAgreement()).thenReturn(mockAgreement);
            when(mockAgreement.getPeriod()).thenReturn(mockPeriod);
            when(mockPeriod.getEndDate()).thenReturn(LocalDate.of(2025, 12, 31));
            when(mockEngagement.getDate()).thenReturn(LocalDate.of(2025, 1, 15));

            // When
            useCase.execute(param);

            // Then
            verify(eventPublisher, never()).publish(any(EngagementCompletedEvent.class));
        }
    }
}
