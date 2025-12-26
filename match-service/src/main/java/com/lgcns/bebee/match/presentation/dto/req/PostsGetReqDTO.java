package com.lgcns.bebee.match.presentation.dto.req;

import com.lgcns.bebee.match.application.usecase.GetPostsUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "게시글 목록 조회 필터 요청 DTO")
public record PostsGetReqDTO(
        @Schema(
                description = """
                        필터링할 법정동 코드 리스트 (선택)
                        - 서울시 법정동 코드 형식 (예: 1168010100 - 강남구 역삼동)
                        - 여러 지역 동시 검색 가능
                        """,
                example = "1168010100,1165010100"
        )
        List<String> legalDongCodes,

        @Schema(
                description = """
                        도움 카테고리 ID 리스트 (선택)
                        - 1: 외출동행
                        - 2: 방문목욕
                        - 3: 방문간호
                        - 4: 가사지원
                        - 5: 식사도움
                        - 6: 학습지원
                        - 7: 정서적 지원
                        - 8: 기타생활지원
                        """,
                example = "1,2,4"
        )
        List<Long> helpCategories,

        @Schema(
                description = """
                        희망하는 도우미 성별 (선택)
                        - MALE: 남성 도우미 선호
                        - FEMALE: 여성 도우미 선호
                        - NONE: 성별 무관
                        """,
                example = "FEMALE"
        )
        String gender,

        @Schema(
                description = "최소 꿀 금액 (선택) - 단위: 원",
                example = "5000"
        )
        Integer minHoney,

        @Schema(
                description = "최대 꿀 금액 (선택) - 단위: 원",
                example = "50000"
        )
        Integer maxHoney,

        @Schema(
                description = """
                        장애 카테고리 ID (선택)
                        - 1: 지체장애
                        - 2: 시각장애
                        - 3: 청각장애
                        """,
                example = "1"
        )
        Long disabilityCategoryId,

        @Schema(
                description = """
                        활동 가능 요일 리스트 (선택)
                        - MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
                        - 영문 대문자 요일명 사용
                        """,
                example = "MONDAY,WEDNESDAY,FRIDAY"
        )
        List<String> days
) {

    public GetPostsUseCase.Param toParam(Long memberId, String postType, Boolean isMatched, String lastPostId, Integer count) {
        Long parsedLastPostId = (lastPostId != null && !lastPostId.isBlank())
                ? Long.parseLong(lastPostId)
                : null;

        return new GetPostsUseCase.Param(
                memberId,
                postType,
                isMatched,
                this.legalDongCodes,
                this.helpCategories,
                this.gender,
                this.minHoney,
                this.maxHoney,
                this.disabilityCategoryId,
                this.days,
                parsedLastPostId,
                count
        );
    }
}
