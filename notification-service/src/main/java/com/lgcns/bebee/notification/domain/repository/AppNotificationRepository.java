package com.lgcns.bebee.notification.domain.repository;

import com.lgcns.bebee.notification.domain.entity.AppNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppNotificationRepository extends JpaRepository<AppNotification, Long> {
}
