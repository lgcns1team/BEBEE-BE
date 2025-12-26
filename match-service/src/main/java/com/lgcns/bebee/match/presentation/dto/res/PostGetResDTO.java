package com.lgcns.bebee.match.presentation.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lgcns.bebee.match.application.usecase.GetSinglePostUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(description = "게시글 상세 조회 응답")
public record PostGetResDTO(
        @Schema(description = "게시글 작성자 닉네임", example = "김철수")
        String memberNickname,

        @Schema(description = "게시글 작성자 법정동 코드", example = "1168010500")
        String memberLegalDongCode,

        @Schema(description = "게시글 작성자 프로필 이미지 URL", example = "https://example.com/profiles/member.jpg")
        String memberProfileImageUrl,

        @Schema(description = "도움 카테고리 목록", example = "[\"외출동행\", \"방문목욕\"]")
        List<String> helpCategories,

        @Schema(description = "도움 타입 (DAY: 일회성, TERM: 정기적)", example = "DAY")
        String engagementType,

        @Schema(description = "게시글 제목", example = "병원 동행 도와주실 분 구합니다")
        String title,

        @Schema(description = "단위 꿀 (시간당 보상)", example = "10000")
        Integer unitHoney,

        @Schema(description = "총 꿀 (전체 보상)", example = "30000")
        Integer totalHoney,

        @Schema(description = "날짜 (DAY 타입인 경우만 사용)", example = "2025-11-21")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDate date,

        @Schema(description = "시작 날짜 (TERM 타입인 경우만 사용)", example = "2025-11-11")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDate startDate,

        @Schema(description = "종료 날짜 (TERM 타입인 경우만 사용)", example = "2025-11-24")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDate endDate,

        @Schema(description = "스케줄 목록 (요일별 시간)")
        List<ScheduleDTO> schedules,

        @Schema(description = "게시글 작성 지역 법정동 코드", example = "1168010500")
        String postLegalDongCode,

        @Schema(description = "게시글 내용", example = "병원 진료 예약이 있어서 동행해주실 분을 찾습니다.")
        String content,

        @Schema(description = "신청자 수", example = "3")
        Integer applicantCount,

        @Schema(description = "게시글 이미지 URL 목록", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
        List<String> postImageUrls
) {
    public static PostGetResDTO from(GetSinglePostUseCase.Result result) {
        return new PostGetResDTO(
                result.getMemberNickname(),
                result.getMemberLegalDongCode(),
                result.getMemberProfileImageUrl(),
                result.getHelpCategories(),
                result.getEngagementType(),
                result.getTitle(),
                result.getUnitHoney(),
                result.getTotalHoney(),
                result.getDate(),
                result.getStartDate(),
                result.getEndDate(),
                result.getSchedules().stream()
                        .map(s -> new ScheduleDTO(s.getDayOfWeek(), s.getStartTime(), s.getEndTime()))
                        .toList(),
                result.getPostLegalDongCode(),
                result.getContent(),
                result.getApplicantCount(),
                result.getPostImageUrls()
        );
    }

    @Schema(description = "스케줄 정보")
    public record ScheduleDTO(
            @Schema(description = "요일", example = "MONDAY")
            String dayOfWeek,

            @Schema(description = "시작 시간", example = "11:00:00")
            LocalTime startTime,

            @Schema(description = "종료 시간", example = "14:00:00")
            LocalTime endTime
    ) {}
}
