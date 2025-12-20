package com.lgcns.bebee.notification.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.notification.domain.entity.vo.NotificationType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class AppNotification extends BaseTimeEntity {
    @Id
    @Tsid
    @Column(name = "app_notification_id")
    private Long id;

    @Column(nullable = false)
    protected Long receiverId;

    @Column(nullable = false)
    protected Long senderId;

    @Column(nullable = false)
    private Boolean isRead;

    @Column
    private LocalDateTime readAt;

    // 해당 필드를 삽입, 수정이 불가능하게 해야 한다.
    @Column(name = "type", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    protected NotificationType type;

    public abstract String getTitle();

    public abstract String getBody();

    public abstract Map<String, String> getData();
}
