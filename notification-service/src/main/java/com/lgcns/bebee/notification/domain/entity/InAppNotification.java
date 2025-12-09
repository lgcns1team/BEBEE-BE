package com.lgcns.bebee.notification.domain.entity;

import com.lgcns.bebee.notification.domain.entity.vo.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public class InAppNotification {
    @Id
    private Long in_app_notification_id;

    @Column(nullable = false)
    private Long receiver_id;

    @Column(nullable = false)
    private Long sender_id;

    @Column(nullable = false)
    private Boolean is_read;

    @Column
    private LocalDateTime read_at;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(nullable = false, length = 255)
    private String redirection_url;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;
}
