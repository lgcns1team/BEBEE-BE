package com.lgcns.bebee.chat.presentation.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lgcns.bebee.chat.domain.entity.Chat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "채팅 메시지 목록 조회 응답")
public record ChatMessagesGetResDTO(
        @Schema(description = "채팅 메시지 목록", example = "[...]")
        List<ChatMessageDTO> messages,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext,

        @Schema(description = "다음 페이지 조회를 위한 커서 ID (hasNext가 true일 때만 제공)", example = "1234567890", nullable = true)
        String nextChatId
) {
    public static ChatMessagesGetResDTO from(List<Chat> messages, boolean hasNext, Long nextChatId) {
        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(ChatMessageDTO::from)
                .toList();

        return ChatMessagesGetResDTO.builder()
                .messages(messageDTOs)
                .hasNext(hasNext)
                .nextChatId(String.valueOf(nextChatId))
                .build();
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "채팅 메시지 상세 정보")
    public record ChatMessageDTO(
            @Schema(description = "메시지 ID (TSID)", example = "1234567890")
            String id,

            @Schema(description = "발신자 ID", example = "100")
            String senderId,

            @Schema(description = "텍스트 메시지 내용", example = "안녕하세요", nullable = true)
            String textContent,

            @Schema(description = "메시지 타입", example = "TEXT", allowableValues = {"TEXT", "IMAGE", "MATCH_CONFIRMATION", "MATCH_SUCCESS", "MATCH_FAILURE"})
            String type,

            @Schema(description = "첨부 파일 URL 목록", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]", nullable = true)
            List<String> attachments,

            @Schema(description = "매칭 확인서 ID (매칭 확인서 메시지인 경우)", example = "5001", nullable = true)
            String agreementId,

            @Schema(description = "매칭 타입 (매칭 확인서 메시지인 경우)", example = "TERM", allowableValues = {"DAY", "TERM"}, nullable = true)
            String matchType,

            @Schema(description = "매칭 시작일 (매칭 확인서 메시지인 경우)", example = "2024-01-15", nullable = true)
            String startDate,

            @Schema(description = "매칭 종료일 (매칭 확인서 메시지인 경우)", example = "2024-06-30", nullable = true)
            String endDate,

            @Schema(description = "일정 요일 목록 (매칭 확인서 메시지인 경우)", example = "[\"월\", \"수\", \"금\"]", nullable = true)
            List<String> scheduleDays,

            @Schema(description = "일정 시작 시간 목록 (매칭 확인서 메시지인 경우)", example = "[\"09:00\", \"09:00\", \"14:00\"]", nullable = true)
            List<String> scheduleStartTimes,

            @Schema(description = "일정 종료 시간 목록 (매칭 확인서 메시지인 경우)", example = "[\"12:00\", \"12:00\", \"17:00\"]", nullable = true)
            List<String> scheduleEndTimes,

            @Schema(description = "매칭 장소 (매칭 확인서 메시지인 경우)", example = "서울시 강남구", nullable = true)
            String location,

            @Schema(description = "단위 포인트 (매칭 확인서 메시지인 경우)", example = "1000", nullable = true)
            Integer unitPoints,

            @Schema(description = "총 포인트 (매칭 확인서 메시지인 경우)", example = "30000", nullable = true)
            Integer totalPoints,

            @Schema(description = "매칭 상태 (매칭 확인서 메시지인 경우)", example = "PENDING", allowableValues = {"PENDING", "ACCEPTED", "REJECTED"}, nullable = true)
            String matchStatus,

            @Schema(description = "메시지 생성 일시", example = "2024-01-15T10:30:00")
            LocalDateTime createdAt
    ) {
        public static ChatMessageDTO from(Chat chat) {
            Chat.MatchConfirmationContent matchConfirmation = chat.getMatchConfirmationContent();

            // MatchConfirmationContent가 없는 경우 처리
            String agreementId = null;
            String matchType = null;
            String startDate = null;
            String endDate = null;
            List<String> scheduleDays = null;
            List<String> scheduleStartTimes = null;
            List<String> scheduleEndTimes = null;
            String location = null;
            Integer unitPoints = null;
            Integer totalPoints = null;
            String matchStatus = null;

            if (matchConfirmation != null) {
                agreementId = String.valueOf(matchConfirmation.getAgreementId());
                matchType = matchConfirmation.getType().name();
                startDate = matchConfirmation.getStartDate();
                endDate = matchConfirmation.getEndDate();
                location = matchConfirmation.getLocation();
                matchStatus = matchConfirmation.getStatus() != null
                        ? matchConfirmation.getStatus().name()
                        : null;

                // Schedule 리스트 분해
                List<Chat.Schedule> schedules = matchConfirmation.getSchedule();
                if (schedules != null && !schedules.isEmpty()) {
                    scheduleDays = new ArrayList<>();
                    scheduleStartTimes = new ArrayList<>();
                    scheduleEndTimes = new ArrayList<>();

                    for (Chat.Schedule schedule : schedules) {
                        scheduleDays.add(schedule.getDay());
                        scheduleStartTimes.add(schedule.getStartTime());
                        scheduleEndTimes.add(schedule.getEndTime());
                    }
                }

                // Points 분해
                Chat.Points points = matchConfirmation.getPoints();
                if (points != null) {
                    unitPoints = points.getUnitPoints();
                    totalPoints = points.getTotal();
                }
            }

            return ChatMessageDTO.builder()
                    .id(String.valueOf(chat.getId()))
                    .senderId(String.valueOf(chat.getSenderId()))
                    .textContent(chat.getTextContent())
                    .type(chat.getType().name())
                    .attachments(chat.getAttachments())
                    .agreementId(agreementId)
                    .matchType(matchType)
                    .startDate(startDate)
                    .endDate(endDate)
                    .scheduleDays(scheduleDays)
                    .scheduleStartTimes(scheduleStartTimes)
                    .scheduleEndTimes(scheduleEndTimes)
                    .location(location)
                    .unitPoints(unitPoints)
                    .totalPoints(totalPoints)
                    .matchStatus(matchStatus)
                    .createdAt(chat.getCreatedAt())
                    .build();
        }
    }
}