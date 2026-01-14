package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.usecase.CreateMemberUseCase;
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
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("회원 동기화 생성 유스케이스 테스트")
class CreateMemberUseCaseTest {

    @Mock
    private MemberManager memberManager;

    @InjectMocks
    private CreateMemberUseCase useCase;

    private Long memberId;
    private String nickname;
    private String gender;
    private String role;
    private LocalDate birthDate;
    private Double latitude;
    private Double longitude;
    private String profileImageUrl;
    private String addressRoad;
    private String legalDongCode;
    private List<Long> disabilityCategoryIds;
    private List<Long> helpCategoryIds;

    @BeforeEach
    void setUp() {
        memberId = 101L;
        nickname = "테스트유저";
        gender = "MALE";
        role = "HELPER";
        birthDate = LocalDate.of(1990, 1, 1);
        latitude = 37.5665;
        longitude = 126.9780;
        profileImageUrl = "https://example.com/profile.jpg";
        addressRoad = "서울특별시 중구 세종대로 110";
        legalDongCode = "1100000000";
        disabilityCategoryIds = List.of(1L, 2L);
        helpCategoryIds = List.of(3L, 4L);
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("모든 파라미터가 유효하면 회원이 생성된다")
        void shouldCreateMember_whenAllParametersAreValid() {
            // Given
            CreateMemberUseCase.Param param = new CreateMemberUseCase.Param(
                    memberId,
                    nickname,
                    gender,
                    role,
                    birthDate,
                    latitude,
                    longitude,
                    profileImageUrl,
                    addressRoad,
                    legalDongCode,
                    disabilityCategoryIds,
                    helpCategoryIds
            );

            doNothing().when(memberManager).createMember(
                    anyLong(), anyString(), anyString(), anyString(),
                    any(LocalDate.class), anyDouble(), anyDouble(),
                    anyString(), anyString(), anyString(),
                    anyList(), anyList()
            );

            // When
            useCase.execute(param);

            // Then
            verify(memberManager, times(1)).createMember(
                    eq(memberId),
                    eq(nickname),
                    eq(gender),
                    eq(role),
                    eq(birthDate),
                    eq(latitude),
                    eq(longitude),
                    eq(profileImageUrl),
                    eq(addressRoad),
                    eq(legalDongCode),
                    eq(disabilityCategoryIds),
                    eq(helpCategoryIds)
            );
        }

        @Test
        @DisplayName("DISABLED 역할로 회원이 생성된다")
        void shouldCreateMember_withDisabledRole() {
            // Given
            CreateMemberUseCase.Param param = new CreateMemberUseCase.Param(
                    memberId,
                    nickname,
                    gender,
                    "DISABLED",
                    birthDate,
                    latitude,
                    longitude,
                    profileImageUrl,
                    addressRoad,
                    legalDongCode,
                    disabilityCategoryIds,
                    helpCategoryIds
            );

            doNothing().when(memberManager).createMember(
                    anyLong(), anyString(), anyString(), anyString(),
                    any(LocalDate.class), anyDouble(), anyDouble(),
                    anyString(), anyString(), anyString(),
                    anyList(), anyList()
            );

            // When
            useCase.execute(param);

            // Then
            verify(memberManager, times(1)).createMember(
                    eq(memberId),
                    eq(nickname),
                    eq(gender),
                    eq("DISABLED"),
                    any(LocalDate.class),
                    anyDouble(),
                    anyDouble(),
                    anyString(),
                    anyString(),
                    anyString(),
                    anyList(),
                    anyList()
            );
        }

        @Test
        @DisplayName("빈 카테고리 리스트로도 회원이 생성된다")
        void shouldCreateMember_withEmptyCategoryLists() {
            // Given
            CreateMemberUseCase.Param param = new CreateMemberUseCase.Param(
                    memberId,
                    nickname,
                    gender,
                    role,
                    birthDate,
                    latitude,
                    longitude,
                    profileImageUrl,
                    addressRoad,
                    legalDongCode,
                    List.of(),
                    List.of()
            );

            doNothing().when(memberManager).createMember(
                    anyLong(), anyString(), anyString(), anyString(),
                    any(LocalDate.class), anyDouble(), anyDouble(),
                    anyString(), anyString(), anyString(),
                    anyList(), anyList()
            );

            // When
            useCase.execute(param);

            // Then
            verify(memberManager, times(1)).createMember(
                    eq(memberId),
                    eq(nickname),
                    eq(gender),
                    eq(role),
                    eq(birthDate),
                    eq(latitude),
                    eq(longitude),
                    eq(profileImageUrl),
                    eq(addressRoad),
                    eq(legalDongCode),
                    eq(List.of()),
                    eq(List.of())
            );
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("memberId가 1일 때 정상 처리된다")
        void shouldCreateMember_whenMemberIdIsOne() {
            // Given
            CreateMemberUseCase.Param param = new CreateMemberUseCase.Param(
                    1L,
                    nickname,
                    gender,
                    role,
                    birthDate,
                    latitude,
                    longitude,
                    profileImageUrl,
                    addressRoad,
                    legalDongCode,
                    disabilityCategoryIds,
                    helpCategoryIds
            );

            doNothing().when(memberManager).createMember(
                    anyLong(), anyString(), anyString(), anyString(),
                    any(LocalDate.class), anyDouble(), anyDouble(),
                    anyString(), anyString(), anyString(),
                    anyList(), anyList()
            );

            // When
            useCase.execute(param);

            // Then
            verify(memberManager, times(1)).createMember(
                    eq(1L),
                    anyString(),
                    anyString(),
                    anyString(),
                    any(LocalDate.class),
                    anyDouble(),
                    anyDouble(),
                    anyString(),
                    anyString(),
                    anyString(),
                    anyList(),
                    anyList()
            );
        }

        @Test
        @DisplayName("profileImageUrl이 null이어도 정상 처리된다")
        void shouldCreateMember_whenProfileImageUrlIsNull() {
            // Given
            CreateMemberUseCase.Param param = new CreateMemberUseCase.Param(
                    memberId,
                    nickname,
                    gender,
                    role,
                    birthDate,
                    latitude,
                    longitude,
                    null,
                    addressRoad,
                    legalDongCode,
                    disabilityCategoryIds,
                    helpCategoryIds
            );

            doNothing().when(memberManager).createMember(
                    anyLong(), anyString(), anyString(), anyString(),
                    any(LocalDate.class), anyDouble(), anyDouble(),
                    any(), anyString(), anyString(),
                    anyList(), anyList()
            );

            // When
            useCase.execute(param);

            // Then
            verify(memberManager, times(1)).createMember(
                    eq(memberId),
                    eq(nickname),
                    eq(gender),
                    eq(role),
                    eq(birthDate),
                    eq(latitude),
                    eq(longitude),
                    isNull(),
                    eq(addressRoad),
                    eq(legalDongCode),
                    eq(disabilityCategoryIds),
                    eq(helpCategoryIds)
            );
        }
    }
}
