package com.lgcns.bebee.chat.presentation.dto.req;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageReqDTO(
    Long receiverId,
    String textContent,
    String type,
    List<String> attachments,
    Long agreementId,
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
) {
    public ChatMessageReqDTO{
        if(type == null) type = "TEXT";
    }
}
