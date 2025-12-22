package com.lgcns.bebee.chat.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lgcns.bebee.chat.domain.entity.Chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatMessageDTO(
        Long id,
        Long chatroomId,
        Long senderId,
        Long receiverId,
        String textContent,
        String chatType,
        List<String> attachments,
        Long agreementId,
        String matchType,
        String startDate,
        String endDate,
        List<String> scheduleDays,
        List<String> scheduleStartTimes,
        List<String> scheduleEndTimes,
        String location,
        Integer unitPoints,
        Integer totalPoints,
        String matchStatus,
        LocalDateTime createdAt
){
    public static ChatMessageDTO from(Long receiverId, Chat chat) {
        Chat.MatchConfirmationContent matchConfirmation = chat.getMatchConfirmationContent();

        // MatchConfirmationContent가 없는 경우 처리
        Long agreementId = null;
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
            agreementId = matchConfirmation.getAgreementId();
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

        return new ChatMessageDTO(
                chat.getId(),
                chat.getChatroomId(),
                chat.getSenderId(),
                receiverId,
                chat.getTextContent(),
                chat.getType().name(),
                chat.getAttachments(),
                agreementId,
                matchType,
                startDate,
                endDate,
                scheduleDays,
                scheduleStartTimes,
                scheduleEndTimes,
                location,
                unitPoints,
                totalPoints,
                matchStatus,
                chat.getCreatedAt()
        );
    }
}