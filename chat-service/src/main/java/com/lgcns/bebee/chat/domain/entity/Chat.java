package com.lgcns.bebee.chat.domain.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

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

    public static class MatchConfirmationContent{
        private Long agreementId;
        private List<Schedule> schedule;
        private Points points;
        private MatchStatus status;
    }

    public static class Schedule{
        private String day;
        @Field("start_time") private String startTime;
        @Field("end_time") private String endTime;
    }

    public static class Points{
        @Field("unit_points") private Integer unitPoints;
        @Field("total") private Integer total;
    }

    public enum ChatType {
        TEXT, IMAGE, MATCH_SUCCESS, MATCH_FAILURE, MATCH_CONFIRMATION
    }

    public enum MatchStatus{
        PENDING, REJECTED, ACCEPTED
    }
}
