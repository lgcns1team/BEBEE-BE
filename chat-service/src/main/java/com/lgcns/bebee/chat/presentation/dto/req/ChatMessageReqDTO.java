package com.lgcns.bebee.chat.presentation.dto.req;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageReqDTO(
    Long chatroomId,
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
) {
    public ChatMessageReqDTO{
        if(chatType == null) chatType = "TEXT";
    }
}
