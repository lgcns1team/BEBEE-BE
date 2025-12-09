package com.lgcns.bebee.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@PrimaryKeyJoinColumn(name = "in_app_notification_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("APPLICATION")
public class ApplicationInAppNotification extends InAppNotification {
    @Column(nullable = false)
    private Long applicationId;
}
