package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.data.event.DomainEventPublisher;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.data.event.match.PostAppliedEvent;
import com.lgcns.bebee.match.application.usecase.ApplyHelperUseCase;
import com.lgcns.bebee.match.common.exception.MatchException;
import com.lgcns.bebee.match.domain.entity.Application;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.PostStatus;
import com.lgcns.bebee.match.domain.repository.HelperApplicationRepository;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.PostManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("도우미 지원 유스케이스 테스트 - ApplyHelperUseCase")
class ApplyHelperUseCaseTest {

    @Mock private MemberManager memberManager;
    @Mock private PostManager postManager;
    @Mock private HelperApplicationRepository applicationRepository;
    @Mock private DomainEventPublisher eventPublisher;

    @InjectMocks private ApplyHelperUseCase useCase;

    private Long memberId;
    private Long postId;
    private Long postOwnerId;

    @BeforeEach
    void setUp() {
        memberId = 101L;
        postId = 1001L;
        postOwnerId = 202L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("정상 지원 시: Application 저장 + 지원자수 증가 + 게시글 저장 + 이벤트 발행")
        void apply_success_should_saveApplication_and_increment_and_publishEvent() {
            // given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, postId, false);

            MemberSync member = mock(MemberSync.class);
            when(member.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(member);

            when(applicationRepository.existsByApplicantIdAndPost_Id(memberId, postId)).thenReturn(false);

            Post post = mock(Post.class);
            when(post.getStatus()).thenReturn(PostStatus.NON_MATCHED);
            when(post.getId()).thenReturn(postId);
            when(post.getTitle()).thenReturn("테스트 게시물");
            when(post.getMemberId()).thenReturn(postOwnerId);
            when(postManager.findSinglePostForUpdate(postId)).thenReturn(post);

            // Application.create()는 실제로 생성되게 두고, save 결과만 mock으로 반환
            Application saved = mock(Application.class);
            when(saved.getApplicantId()).thenReturn(memberId);
            when(saved.getApplicationId()).thenReturn(1L);
            when(saved.getPost()).thenReturn(post);
            when(applicationRepository.save(any(Application.class))).thenReturn(saved);

            // when
            Void result = useCase.execute(param);

            // then
            assertThat(result).isNull();

            verify(memberManager).findExistingMember(memberId);
            verify(applicationRepository).existsByApplicantIdAndPost_Id(memberId, postId);
            verify(postManager).findSinglePostForUpdate(postId);

            verify(applicationRepository).save(any(Application.class));
            verify(post).incrementApplicantCount();
            verify(postManager).savePost(post);

            verify(eventPublisher).publish(any(PostAppliedEvent.class));
        }

        @Test
        @DisplayName("isVolunteer=true여도 정상 처리된다")
        void apply_success_when_isVolunteer_true() {
            // given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, postId, true);

            MemberSync member = mock(MemberSync.class);
            when(member.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(member);

            when(applicationRepository.existsByApplicantIdAndPost_Id(memberId, postId)).thenReturn(false);

            Post post = mock(Post.class);
            when(post.getStatus()).thenReturn(PostStatus.NON_MATCHED);
            when(post.getId()).thenReturn(postId);
            when(post.getTitle()).thenReturn("테스트 게시물");
            when(post.getMemberId()).thenReturn(postOwnerId);
            when(postManager.findSinglePostForUpdate(postId)).thenReturn(post);

            Application saved = mock(Application.class);
            when(saved.getApplicantId()).thenReturn(memberId);
            when(saved.getApplicationId()).thenReturn(1L);
            when(saved.getPost()).thenReturn(post);
            when(applicationRepository.save(any(Application.class))).thenReturn(saved);

            // when
            useCase.execute(param);

            // then
            verify(applicationRepository).save(any(Application.class));
            verify(post).incrementApplicantCount();
            verify(eventPublisher).publish(any(PostAppliedEvent.class));
        }
    }

    @Nested
    @DisplayName("파라미터 검증 실패")
    class ParamValidationFailures {

        @Test
        @DisplayName("memberId가 null이면 InvalidParamException")
        void memberId_null_should_throw() {
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(null, postId, false);

            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class);

            verifyNoInteractions(memberManager, postManager, applicationRepository, eventPublisher);
        }

        @Test
        @DisplayName("memberId가 0 이하이면 InvalidParamException")
        void memberId_invalid_should_throw() {
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(0L, postId, false);

            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class);

            verifyNoInteractions(memberManager, postManager, applicationRepository, eventPublisher);
        }

        @Test
        @DisplayName("postId가 null이면 InvalidParamException")
        void postId_null_should_throw() {
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, null, false);

            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class);

            verifyNoInteractions(memberManager, postManager, applicationRepository, eventPublisher);
        }

        @Test
        @DisplayName("postId가 0 이하이면 InvalidParamException")
        void postId_invalid_should_throw() {
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, -1L, false);

            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class);

            verifyNoInteractions(memberManager, postManager, applicationRepository, eventPublisher);
        }

        @Test
        @DisplayName("isVolunteer가 null이면 InvalidParamException")
        void isVolunteer_null_should_throw() {
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, postId, null);

            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class);

            verifyNoInteractions(memberManager, postManager, applicationRepository, eventPublisher);
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 실패")
    class BusinessFailures {

        @Test
        @DisplayName("HELPER가 아니면 MatchException (도우미만 지원 가능)")
        void not_helper_should_throw() {
            // given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, postId, false);

            MemberSync member = mock(MemberSync.class);
            when(member.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(memberId)).thenReturn(member);

            // when & then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);

            verify(memberManager).findExistingMember(memberId);
            verify(applicationRepository, never()).existsByApplicantIdAndPost_Id(anyLong(), anyLong());
            verify(postManager, never()).findSinglePostForUpdate(anyLong());
            verify(applicationRepository, never()).save(any(Application.class));
            verify(eventPublisher, never()).publish(any());
        }

        @Test
        @DisplayName("이미 지원한 게시글이면 MatchException (중복 지원 불가)")
        void already_applied_should_throw() {
            // given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, postId, false);

            MemberSync member = mock(MemberSync.class);
            when(member.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(member);

            when(applicationRepository.existsByApplicantIdAndPost_Id(memberId, postId)).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);

            verify(applicationRepository).existsByApplicantIdAndPost_Id(memberId, postId);
            verify(postManager, never()).findSinglePostForUpdate(anyLong());
            verify(applicationRepository, never()).save(any(Application.class));
            verify(eventPublisher, never()).publish(any());
        }

        @Test
        @DisplayName("게시글이 이미 MATCHED면 MatchException (이미 매칭됨)")
        void post_already_matched_should_throw() {
            // given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, postId, false);

            MemberSync member = mock(MemberSync.class);
            when(member.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(member);

            when(applicationRepository.existsByApplicantIdAndPost_Id(memberId, postId)).thenReturn(false);

            Post post = mock(Post.class);
            when(post.getStatus()).thenReturn(PostStatus.MATCHED);
            when(postManager.findSinglePostForUpdate(postId)).thenReturn(post);

            // when & then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);

            verify(postManager).findSinglePostForUpdate(postId);
            verify(applicationRepository, never()).save(any(Application.class));
            verify(eventPublisher, never()).publish(any());
        }
    }
}
