package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.client.RegionCodeClient;
import com.lgcns.bebee.match.application.usecase.UpdatePostLegalDongCodeUseCase;
import com.lgcns.bebee.match.domain.entity.Post;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("게시물 법정동 코드 업데이트 유스케이스 테스트")
class UpdatePostLegalDongCodeUseCaseTest {

    @Mock
    private PostManager postManager;

    @Mock
    private RegionCodeClient regionCodeClient;

    @InjectMocks
    private UpdatePostLegalDongCodeUseCase useCase;

    private Long postId;
    private Double latitude;
    private Double longitude;

    @BeforeEach
    void setUp() {
        postId = 1L;
        latitude = 37.5665;
        longitude = 126.9780;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("좌표로 법정동 코드를 조회하여 게시물을 업데이트한다")
        void shouldUpdatePostWithLegalDongCode() {
            // Given
            UpdatePostLegalDongCodeUseCase.Param param = new UpdatePostLegalDongCodeUseCase.Param(
                    postId, latitude, longitude
            );

            String legalDongCode = "1100000000";
            when(regionCodeClient.resolveLegalDongCode(latitude, longitude)).thenReturn(legalDongCode);

            Post mockPost = mock(Post.class);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            // When
            useCase.execute(param);

            // Then
            verify(regionCodeClient, times(1)).resolveLegalDongCode(latitude, longitude);
            verify(postManager, times(1)).findSinglePost(postId);
            verify(mockPost, times(1)).updateLegalDongCode(legalDongCode);
        }

        @Test
        @DisplayName("다른 좌표로 법정동 코드를 업데이트한다")
        void shouldUpdateWithDifferentCoordinates() {
            // Given
            Double newLatitude = 37.4979;
            Double newLongitude = 127.0276;

            UpdatePostLegalDongCodeUseCase.Param param = new UpdatePostLegalDongCodeUseCase.Param(
                    postId, newLatitude, newLongitude
            );

            String legalDongCode = "1168000000"; // 강남구
            when(regionCodeClient.resolveLegalDongCode(newLatitude, newLongitude)).thenReturn(legalDongCode);

            Post mockPost = mock(Post.class);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            // When
            useCase.execute(param);

            // Then
            verify(regionCodeClient, times(1)).resolveLegalDongCode(newLatitude, newLongitude);
            verify(mockPost, times(1)).updateLegalDongCode(legalDongCode);
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("postId가 1일 때 정상 처리된다")
        void shouldWork_whenPostIdIsOne() {
            // Given
            UpdatePostLegalDongCodeUseCase.Param param = new UpdatePostLegalDongCodeUseCase.Param(
                    1L, latitude, longitude
            );

            String legalDongCode = "1100000000";
            when(regionCodeClient.resolveLegalDongCode(latitude, longitude)).thenReturn(legalDongCode);

            Post mockPost = mock(Post.class);
            when(postManager.findSinglePost(1L)).thenReturn(mockPost);

            // When
            useCase.execute(param);

            // Then
            verify(postManager, times(1)).findSinglePost(1L);
            verify(mockPost, times(1)).updateLegalDongCode(legalDongCode);
        }

        @Test
        @DisplayName("위도 0에서 정상 처리된다")
        void shouldWork_whenLatitudeIsZero() {
            // Given
            UpdatePostLegalDongCodeUseCase.Param param = new UpdatePostLegalDongCodeUseCase.Param(
                    postId, 0.0, longitude
            );

            String legalDongCode = "UNKNOWN";
            when(regionCodeClient.resolveLegalDongCode(0.0, longitude)).thenReturn(legalDongCode);

            Post mockPost = mock(Post.class);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            // When
            useCase.execute(param);

            // Then
            verify(regionCodeClient, times(1)).resolveLegalDongCode(0.0, longitude);
            verify(mockPost, times(1)).updateLegalDongCode(legalDongCode);
        }

        @Test
        @DisplayName("경도 0에서 정상 처리된다")
        void shouldWork_whenLongitudeIsZero() {
            // Given
            UpdatePostLegalDongCodeUseCase.Param param = new UpdatePostLegalDongCodeUseCase.Param(
                    postId, latitude, 0.0
            );

            String legalDongCode = "UNKNOWN";
            when(regionCodeClient.resolveLegalDongCode(latitude, 0.0)).thenReturn(legalDongCode);

            Post mockPost = mock(Post.class);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            // When
            useCase.execute(param);

            // Then
            verify(regionCodeClient, times(1)).resolveLegalDongCode(latitude, 0.0);
            verify(mockPost, times(1)).updateLegalDongCode(legalDongCode);
        }

        @Test
        @DisplayName("음수 좌표에서도 정상 처리된다")
        void shouldWork_withNegativeCoordinates() {
            // Given
            Double negativeLatitude = -33.8688;
            Double negativeLongitude = -151.2093;

            UpdatePostLegalDongCodeUseCase.Param param = new UpdatePostLegalDongCodeUseCase.Param(
                    postId, negativeLatitude, negativeLongitude
            );

            String legalDongCode = "FOREIGN";
            when(regionCodeClient.resolveLegalDongCode(negativeLatitude, negativeLongitude))
                    .thenReturn(legalDongCode);

            Post mockPost = mock(Post.class);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            // When
            useCase.execute(param);

            // Then
            verify(regionCodeClient, times(1)).resolveLegalDongCode(negativeLatitude, negativeLongitude);
            verify(mockPost, times(1)).updateLegalDongCode(legalDongCode);
        }
    }
}
