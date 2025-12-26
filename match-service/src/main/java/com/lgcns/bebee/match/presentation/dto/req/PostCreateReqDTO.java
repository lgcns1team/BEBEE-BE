package com.lgcns.bebee.match.presentation.dto.req;

import com.lgcns.bebee.match.application.usecase.CreatePostUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(description = "게시글 작성 요청 DTO")
public record PostCreateReqDTO(
        @Schema(
                description = "게시글 타입 (DAY: 일회성, TERM: 정기적)",
                example = "DAY",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"DAY", "TERM"}
        )
        String postType,

        @Schema(
                description = "게시글 이미지 URL 목록 (최대 3개)",
                example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        List<String> postImages,

        @Schema(
                description = "게시글 제목",
                example = "어르신 외출 동행 도와주실 분",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String title,

        @Schema(
                description = "도움 카테고리 ID 목록 (최소 1개 이상)",
                example = "[1,3,5]",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        List<Long> helpCategoryIds,

        @Schema(
                description = "게시글 내용",
                example = "병원 방문 시 동행해주실 분을 찾습니다.",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String content,

        @Schema(
                description = "시작 날짜 (TERM 타입일 때 필수)",
                example = "2025-01-01",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        LocalDate startDate,

        @Schema(
                description = "종료 날짜 (TERM 타입일 때 필수)",
                example = "2025-01-31",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        LocalDate endDate,

        @Schema(
                description = "활동 가능 요일 및 시간 목록 (TERM 타입일 때 필수)",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        List<ScheduleDTO> schedules,

        @Schema(
                description = "활동 날짜 (DAY 타입일 때 필수)",
                example = "2025-01-15",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        LocalDate date,

        @Schema(
                description = "단위 꿀 (시간당 또는 일당 보상)",
                example = "15000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer unitHoney,

        @Schema(
                description = "총 꿀 (전체 보상 금액)",
                example = "150000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer totalHoney,

        @Schema(
                description = "지역명",
                example = "서울특별시 강남구 역삼동",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String region,

        @Schema(
                description = "법정동 코드",
                example = "1168010100",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String legalDongCode,

        @Schema(
                description = "위도",
                example = "37.5012767241426",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal latitude,

        @Schema(
                description = "경도",
                example = "127.039604663862",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        BigDecimal longitude
) {
    public CreatePostUseCase.Param toParam(String memberId) {
        List<CreatePostUseCase.ScheduleParam> scheduleParams = schedules.stream()
                .map(s -> new CreatePostUseCase.ScheduleParam(s.dayOfWeek(), s.startTime(), s.endTime()))
                .toList();

        return new CreatePostUseCase.Param(
                Long.parseLong(memberId),
                postType,
                postImages,
                title,
                helpCategoryIds,
                content,
                startDate,
                endDate,
                scheduleParams,
                date,
                unitHoney,
                totalHoney,
                region,
                legalDongCode,
                latitude,
                longitude
        );
    }

    @Schema(description = "활동 가능 요일 및 시간")
    public record ScheduleDTO(
            @Schema(
                    description = "활동 가능 요일",
                    example = "MONDAY",
                    requiredMode = Schema.RequiredMode.REQUIRED,
                    allowableValues = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"}
            )
            DayOfWeek dayOfWeek,

            @Schema(
                    description = "시작 시간",
                    example = "09:00:00",
                    requiredMode = Schema.RequiredMode.REQUIRED
            )
            LocalTime startTime,

            @Schema(
                    description = "종료 시간",
                    example = "18:00:00",
                    requiredMode = Schema.RequiredMode.REQUIRED
            )
            LocalTime endTime
    ) {
    }
}
