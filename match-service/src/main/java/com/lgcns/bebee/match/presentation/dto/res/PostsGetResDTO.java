package com.lgcns.bebee.match.presentation.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lgcns.bebee.match.application.usecase.GetPostsUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "게시글 목록 조회 응답 DTO")
public record PostsGetResDTO(
        @Schema(
                description = """
                        다음 페이지 존재 여부
                        - true: 다음 페이지가 있음
                        - false: 마지막 페이지
                        """,
                example = "true"
        )
        Boolean hasNext,

        @Schema(
                description = """
                        다음 페이지를 위한 커서 값
                        - null이면 더 이상 데이터가 없음
                        - 다음 페이지 요청 시 이 값을 cursor 파라미터로 전달
                        """,
                example = "1030",
                nullable = true
        )
        @JsonInclude(JsonInclude.Include.NON_NULL) String nextPostId,

        @Schema(
                description = "조회된 게시글 목록 - NON_MATCHED 상태의 게시글만 포함",
                example = "[]"
        )
        List<PostResDTO> posts
) {

    public static PostsGetResDTO from(GetPostsUseCase.Result result) {
        List<PostResDTO> postResDTOs = result.getPosts().stream()
                .map(postDTO -> new PostResDTO(
                        String.valueOf(postDTO.getPostId()),
                        postDTO.getTitle(),
                        postDTO.getUnitHoney(),
                        postDTO.getTotalHoney(),
                        postDTO.getLegalDongName(),
                        postDTO.getHelpCategories(),
                        postDTO.getHelpType(),
                        postDTO.getImageUrl(),
                        postDTO.getDate(),
                        postDTO.getDayOfWeeks()
                ))
                .toList();

        return new PostsGetResDTO(result.getHasNext(), result.getNextPostId() != null ? String.valueOf(result.getNextPostId()) : null, postResDTOs);
    }

    @Schema(description = "게시글 요약 정보")
    public record PostResDTO(
            @Schema(
                    description = "게시글 고유 ID",
                    example = "1001"
            )
            String postId,

            @Schema(
                    description = "게시글 제목",
                    example = "병원 동행 도와주실 분"
            )
            String title,

            @Schema(
                    description = "단위 꿀 금액 (회당 보상) - 단위: 원",
                    example = "5000"
            )
            Integer unitHoney,

            @Schema(
                    description = "총 꿀 금액 (전체 보상) - 단위: 원",
                    example = "50000"
            )
            Integer totalHoney,

            @Schema(
                    description = "활동 지역 (법정동 이름)",
                    example = "서울시 강남구 역삼동"
            )
            String legalDongName,

            @Schema(
                    description = "도움 카테고리 이름 리스트",
                    example = "[\"외출동행\", \"방문간호\"]"
            )
            List<String> helpCategories,

            @Schema(
                    description = "도움 타입 - DAY(일회성) 또는 TERM(정기적)",
                    example = "DAY"
            )
            String helpType,

            @Schema(
                    description = "게시글 대표 이미지 URL (없으면 null)",
                    example = "https://example.com/posts/1001/img1.jpg",
                    nullable = true
            )
            String imageUrl,

            @Schema(
                    description = "활동 날짜 (DAY 타입인 경우에만 값 존재)",
                    example = "2024-12-25",
                    nullable = true
            )
            @JsonInclude(JsonInclude.Include.NON_NULL) LocalDate date,

            @Schema(
                    description = "활동 요일 목록",
                    example = "[\"MONDAY\", \"WEDNESDAY\", \"FRIDAY\"]",
                    nullable = true
            )
            @JsonInclude(JsonInclude.Include.NON_NULL) List<String> dayOfWeeks
    ){

    }
}
