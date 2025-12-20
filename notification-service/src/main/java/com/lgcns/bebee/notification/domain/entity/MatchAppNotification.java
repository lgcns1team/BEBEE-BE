package com.lgcns.bebee.notification.domain.entity;

import com.lgcns.bebee.notification.domain.entity.vo.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Entity
@PrimaryKeyJoinColumn(name = "in_app_notification_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("MATCH")
public class MatchAppNotification extends AppNotification {
    @Column(nullable = false)
    private Long matchId;

    public static MatchAppNotification create(Long senderId, Long receiverId, Long matchId){
        MatchAppNotification notification = new MatchAppNotification();
        notification.senderId = senderId;
        notification.receiverId = receiverId;
        notification.type = NotificationType.MATCH;
        notification.matchId = matchId;

        return notification;
    }

    @Override
    public String getTitle() {
        return "매칭이 완료되었습니다";
    }

    @Override
    public String getBody() {
        return "새로운 매칭이 성사되었습니다.";
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("type", NotificationType.MATCH.name());
        data.put("senderId", String.valueOf(senderId));
        data.put("receiverId", String.valueOf(receiverId));
        data.put("matchId", String.valueOf(matchId));
        return data;
    }
}
