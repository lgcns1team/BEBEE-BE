package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.data.event.DomainEventPublisher;
import com.lgcns.bebee.common.data.event.match.PostCreatedEvent;
import com.lgcns.bebee.match.application.usecase.CreatePostUseCase;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("게시물 생성 유스케이스 테스트")
class CreatePostUseCaseTest {

    @Mock
    private MemberManager memberManager;

    @Mock
    private PostManager postManager;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private CreatePostUseCase useCase;

    private Long memberId;
    private String postType;
    private List<String> postImages;
    private String title;
    private List<Long> helpCategoryIds;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate date;
    private Long unitHoney;
    private Long totalHoney;
    private String region;
    private Double latitude;
    private Double longitude;

    @BeforeEach
    void setUp() {
        memberId = 101L;
        postType = "DAY";
        postImages = List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg");
        title = "도움 요청합니다";
        helpCategoryIds = List.of(1L, 2L);
        content = "도움이 필요합니다.";
        startDate = LocalDate.of(2025, 1, 15);
        endDate = LocalDate.of(2025, 1, 15);
        date = LocalDate.of(2025, 1, 15);
        unitHoney = 5000L;
        totalHoney = 5000L;
        region = "서울특별시 중구";
        latitude = 37.5665;
        longitude = 126.9780;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("모든 파라미터가 유효하면 Post가 생성된다")
        void shouldCreatePost_whenAllParametersAreValid() throws Exception {
            // Given
            List<CreatePostUseCase.ScheduleParam> schedules = List.of(
                    new CreatePostUseCase.ScheduleParam(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0))
            );

            CreatePostUseCase.Param param = new CreatePostUseCase.Param(
                    memberId,
                    postType,
                    postImages,
                    title,
                    helpCategoryIds,
                    content,
                    startDate,
                    endDate,
                    schedules,
                    date,
                    unitHoney,
                    totalHoney,
                    region,
                    latitude,
                    longitude
            );

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getId()).thenReturn(memberId);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Post mockPost = mock(Post.class);
            when(mockPost.getId()).thenReturn(1L);
            when(mockPost.getLatitude()).thenReturn(latitude);
            when(mockPost.getLongitude()).thenReturn(longitude);
            when(postManager.createPost(
                    anyLong(), anyString(), anyList(), anyString(), anyList(),
                    anyString(), any(), any(), any(), anyList(), anyList(), anyList(),
                    anyLong(), anyLong(), anyString(), anyDouble(), anyDouble()
            )).thenReturn(mockPost);

            // When
            CreatePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getPostId()).isEqualTo(1L);
            verify(memberManager, times(1)).findExistingMember(memberId);
            verify(postManager, times(1)).createPost(
                    anyLong(), anyString(), anyList(), anyString(), anyList(),
                    anyString(), any(), any(), any(), anyList(), anyList(), anyList(),
                    anyLong(), anyLong(), anyString(), anyDouble(), anyDouble()
            );
            verify(eventPublisher, times(1)).publish(any(PostCreatedEvent.class));
        }

        @Test
        @DisplayName("TERM 타입으로 Post가 생성된다")
        void shouldCreatePost_whenTypeIsTerm() throws Exception {
            // Given
            List<CreatePostUseCase.ScheduleParam> schedules = List.of(
                    new CreatePostUseCase.ScheduleParam(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)),
                    new CreatePostUseCase.ScheduleParam(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(17, 0))
            );

            CreatePostUseCase.Param param = new CreatePostUseCase.Param(
                    memberId,
                    "TERM",
                    postImages,
                    title,
                    helpCategoryIds,
                    content,
                    LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 3, 31),
                    schedules,
                    null,
                    10000L,
                    150000L,
                    region,
                    latitude,
                    longitude
            );

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getId()).thenReturn(memberId);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Post mockPost = mock(Post.class);
            when(mockPost.getId()).thenReturn(2L);
            when(mockPost.getLatitude()).thenReturn(latitude);
            when(mockPost.getLongitude()).thenReturn(longitude);
            when(postManager.createPost(
                    anyLong(), anyString(), anyList(), anyString(), anyList(),
                    anyString(), any(), any(), any(), anyList(), anyList(), anyList(),
                    anyLong(), anyLong(), anyString(), anyDouble(), anyDouble()
            )).thenReturn(mockPost);

            // When
            CreatePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getPostId()).isEqualTo(2L);
            verify(eventPublisher, times(1)).publish(any(PostCreatedEvent.class));
        }

        @Test
        @DisplayName("빈 이미지 리스트로도 Post가 생성된다")
        void shouldCreatePost_withEmptyImageList() throws Exception {
            // Given
            List<CreatePostUseCase.ScheduleParam> schedules = List.of(
                    new CreatePostUseCase.ScheduleParam(DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(15, 0))
            );

            CreatePostUseCase.Param param = new CreatePostUseCase.Param(
                    memberId,
                    postType,
                    List.of(),
                    title,
                    helpCategoryIds,
                    content,
                    startDate,
                    endDate,
                    schedules,
                    date,
                    unitHoney,
                    totalHoney,
                    region,
                    latitude,
                    longitude
            );

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getId()).thenReturn(memberId);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Post mockPost = mock(Post.class);
            when(mockPost.getId()).thenReturn(3L);
            when(mockPost.getLatitude()).thenReturn(latitude);
            when(mockPost.getLongitude()).thenReturn(longitude);
            when(postManager.createPost(
                    anyLong(), anyString(), anyList(), anyString(), anyList(),
                    anyString(), any(), any(), any(), anyList(), anyList(), anyList(),
                    anyLong(), anyLong(), anyString(), anyDouble(), anyDouble()
            )).thenReturn(mockPost);

            // When
            CreatePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(postManager, times(1)).createPost(
                    anyLong(), anyString(), eq(List.of()), anyString(), anyList(),
                    anyString(), any(), any(), any(), anyList(), anyList(), anyList(),
                    anyLong(), anyLong(), anyString(), anyDouble(), anyDouble()
            );
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 검증")
    class BusinessLogicTests {

        @Test
        @DisplayName("Post 생성 후 PostCreatedEvent가 발행된다")
        void shouldPublishPostCreatedEvent_afterPostCreation() throws Exception {
            // Given
            List<CreatePostUseCase.ScheduleParam> schedules = List.of(
                    new CreatePostUseCase.ScheduleParam(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(12, 0))
            );

            CreatePostUseCase.Param param = new CreatePostUseCase.Param(
                    memberId,
                    postType,
                    postImages,
                    title,
                    helpCategoryIds,
                    content,
                    startDate,
                    endDate,
                    schedules,
                    date,
                    unitHoney,
                    totalHoney,
                    region,
                    latitude,
                    longitude
            );

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getId()).thenReturn(memberId);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Post mockPost = mock(Post.class);
            when(mockPost.getId()).thenReturn(1L);
            when(mockPost.getLatitude()).thenReturn(latitude);
            when(mockPost.getLongitude()).thenReturn(longitude);
            when(postManager.createPost(
                    anyLong(), anyString(), anyList(), anyString(), anyList(),
                    anyString(), any(), any(), any(), anyList(), anyList(), anyList(),
                    anyLong(), anyLong(), anyString(), anyDouble(), anyDouble()
            )).thenReturn(mockPost);

            // When
            useCase.execute(param);

            // Then
            verify(eventPublisher, times(1)).publish(any(PostCreatedEvent.class));
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("memberId가 1일 때 정상 처리된다")
        void shouldCreatePost_whenMemberIdIsOne() throws Exception {
            // Given
            List<CreatePostUseCase.ScheduleParam> schedules = List.of(
                    new CreatePostUseCase.ScheduleParam(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0))
            );

            CreatePostUseCase.Param param = new CreatePostUseCase.Param(
                    1L,
                    postType,
                    postImages,
                    title,
                    helpCategoryIds,
                    content,
                    startDate,
                    endDate,
                    schedules,
                    date,
                    unitHoney,
                    totalHoney,
                    region,
                    latitude,
                    longitude
            );

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getId()).thenReturn(1L);
            when(memberManager.findExistingMember(1L)).thenReturn(mockMember);

            Post mockPost = mock(Post.class);
            when(mockPost.getId()).thenReturn(1L);
            when(mockPost.getLatitude()).thenReturn(latitude);
            when(mockPost.getLongitude()).thenReturn(longitude);
            when(postManager.createPost(
                    anyLong(), anyString(), anyList(), anyString(), anyList(),
                    anyString(), any(), any(), any(), anyList(), anyList(), anyList(),
                    anyLong(), anyLong(), anyString(), anyDouble(), anyDouble()
            )).thenReturn(mockPost);

            // When
            CreatePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(memberManager, times(1)).findExistingMember(1L);
        }

        @Test
        @DisplayName("unitHoney가 0일 때 정상 처리된다")
        void shouldCreatePost_whenUnitHoneyIsZero() throws Exception {
            // Given
            List<CreatePostUseCase.ScheduleParam> schedules = List.of(
                    new CreatePostUseCase.ScheduleParam(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0))
            );

            CreatePostUseCase.Param param = new CreatePostUseCase.Param(
                    memberId,
                    postType,
                    postImages,
                    title,
                    helpCategoryIds,
                    content,
                    startDate,
                    endDate,
                    schedules,
                    date,
                    0L,
                    0L,
                    region,
                    latitude,
                    longitude
            );

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getId()).thenReturn(memberId);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Post mockPost = mock(Post.class);
            when(mockPost.getId()).thenReturn(1L);
            when(mockPost.getLatitude()).thenReturn(latitude);
            when(mockPost.getLongitude()).thenReturn(longitude);
            when(postManager.createPost(
                    anyLong(), anyString(), anyList(), anyString(), anyList(),
                    anyString(), any(), any(), any(), anyList(), anyList(), anyList(),
                    anyLong(), anyLong(), anyString(), anyDouble(), anyDouble()
            )).thenReturn(mockPost);

            // When
            CreatePostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
        }
    }
}
