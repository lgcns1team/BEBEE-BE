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
@DiscriminatorValue("APPLICATION")
public class ApplicationAppNotification extends AppNotification {
    @Column(nullable = false)
    private Long applicationId;

    public static ApplicationAppNotification create(
            Long senderId,
            Long receiverId,
            Long applicationId
    ){
        ApplicationAppNotification inAppNotification = new ApplicationAppNotification();
        inAppNotification.senderId = senderId;
        inAppNotification.receiverId = receiverId;
        inAppNotification.type = NotificationType.APPLICATION;
        inAppNotification.applicationId = applicationId;
        return inAppNotification;
    }

    @Override
    public String getTitle() {
        return "새로운 신청이 있습니다";
    }

    @Override
    public String getBody() {
        return "도움 요청에 새로운 신청이 있습니다.";
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("type", NotificationType.APPLICATION.name());
        data.put("senderId", String.valueOf(senderId));
        data.put("receiverId", String.valueOf(receiverId));
        data.put("applicationId", String.valueOf(applicationId));
        return data;
    }
}
