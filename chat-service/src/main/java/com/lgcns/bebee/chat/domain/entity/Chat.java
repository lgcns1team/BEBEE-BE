package com.lgcns.bebee.chat.domain.entity;

import com.lgcns.bebee.chat.core.exception.ChatInvalidParamErrors;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Document(collection = "chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat{
    @Id
    @Tsid
    private Long id;

    @Field(name = "chatroom_id")
    private Long chatroomId;

    @Field(name = "sender_id")
    private Long senderId;

    @Field(name = "text_content")
    private String textContent;

    private ChatType type;

    private List<String> attachments;

    @Field("match_confirmation_content")
    private MatchConfirmationContent matchConfirmationContent;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Getter
    public static class MatchConfirmationContent{
        private Long agreementId;
        @Field("start_date") private String startDate;
        @Field("end_date") private String endDate;
        private List<Schedule> schedule;
        private String location;
        private Points points;
        @Field("help_categories") private List<String> helpCategories;
        private MatchStatus status;
    }

    @Getter
    public static class Schedule{
        private String day;
        @Field("start_time") private String startTime;
        @Field("end_time") private String endTime;
    }

    @Getter
    public static class Points{
        @Field("unit_points") private Integer unitPoints;
        @Field("total") private Integer total;
    }

    public enum ChatType {
        TEXT, IMAGE, MATCH_SUCCESS, MATCH_FAILURE, MATCH_CONFIRMATION;

        private static ChatType from(String type){
            try{
                return valueOf(type.toUpperCase());
            }catch(IllegalArgumentException e){
                throw ChatInvalidParamErrors.INVALID_CHAT_TYPE.toException();
            }
        }
    }

    public enum MatchStatus{
        PENDING, REJECTED, ACCEPTED;

        private static MatchStatus from(String status){
            try{
                return valueOf(status.toUpperCase());
            }catch(IllegalArgumentException e){
                throw ChatInvalidParamErrors.INVALID_MATCH_STATUS.toException();
            }
        }
    }

    /**
     * Chat 엔티티를 생성하는 정적 팩토리 메서드입니다.
     *
     * @param chatroomId 채팅방 ID
     * @param senderId 발신자 ID
     * @param textContent 텍스트 내용
     * @param type 채팅 타입 (String)
     * @param attachments 첨부파일 목록
     * @param agreementId 계약 ID (매칭 확인 시)
     * @param startDate 일정 시작일 (YYYY.MM.DD 형식)
     * @param endDate 일정 종료일 (YYYY.MM.DD 형식)
     * @param scheduleDays 일정 요일 목록
     * @param scheduleStartTimes 일정 시작 시간 목록 (HH:mm 형식)
     * @param scheduleEndTimes 일정 종료 시간 목록 (HH:mm 형식)
     * @param location 만남 장소
     * @param unitPoints 단위 포인트
     * @param totalPoints 총 포인트
     * @param matchStatus 매칭 상태 (String)
     * @param createdAt 채팅 생성 시간
     * @return 생성된 Chat 엔티티
     */
    public static Chat create(
            Long chatroomId,
            Long senderId,
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
        Chat chat = new Chat();
        chat.chatroomId = chatroomId;
        chat.senderId = senderId;
        chat.textContent = textContent;
        chat.type = ChatType.from(type);
        chat.attachments = attachments;

        // MatchConfirmationContent가 필요한 경우에만 생성
        if (agreementId != null){
            MatchConfirmationContent content = new MatchConfirmationContent();
            content.agreementId = agreementId;
            content.startDate = startDate;
            content.endDate = endDate;
            content.schedule = (scheduleDays != null && !scheduleDays.isEmpty()) ? createSchedules(scheduleDays, scheduleStartTimes, scheduleEndTimes) : null;
            content.location = location;
            content.points = unitPoints != null ? createPoints(unitPoints, totalPoints) : null;
            content.status = matchStatus != null ? MatchStatus.from(matchStatus) : null;
            chat.matchConfirmationContent = content;
        }

        chat.createdAt = createdAt;
        return chat;
    }

    private static List<Schedule> createSchedules(List<String> days, List<String> startTimes, List<String> endTimes) {
        List<Schedule> schedules = new java.util.ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            Schedule schedule = new Schedule();
            schedule.day = days.get(i);
            schedule.startTime = startTimes.get(i);
            schedule.endTime = endTimes.get(i);
            schedules.add(schedule);
        }
        return schedules;
    }

    private static Points createPoints(Integer unitPoints, Integer totalPoints) {
        Points points = new Points();
        points.unitPoints = unitPoints;
        points.total = totalPoints;
        return points;
    }
}
