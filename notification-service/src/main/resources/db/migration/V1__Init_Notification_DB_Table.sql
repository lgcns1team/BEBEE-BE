SET FOREIGN_KEY_CHECKS = 0;

# DROP TABLES
DROP TABLE IF EXISTS application_in_app_notification;
DROP TABLE IF EXISTS chat_in_app_notification;
DROP TABLE IF EXISTS match_in_app_notification;
DROP TABLE IF EXISTS in_app_notification;
DROP TABLE IF EXISTS push_notification_subscription;

# 1. InAppNotification 테이블
CREATE TABLE in_app_notification
(
    in_app_notification_id BIGINT       NOT NULL PRIMARY KEY,
    receiver_id            BIGINT       NOT NULL,
    sender_id              BIGINT       NOT NULL,
    is_read                BOOLEAN      NOT NULL,
    read_at                TIMESTAMP,
    type                   ENUM ('CHAT', 'APPLICATION', 'MATCH'),
    redirection_url        VARCHAR(255) NOT NULL,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


# 2. ChatInAppNotification 테이블
CREATE TABLE chat_in_app_notification
(
    in_app_notification_id BIGINT NOT NULL PRIMARY KEY,
    chat_room_id BIGINT NOT NULL,
    message_preview VARCHAR(255),
    message_count INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_chat_TO_notification
       FOREIGN KEY (in_app_notification_id)
           REFERENCES in_app_notification(in_app_notification_id)
           ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

# 3. ApplicationInAppNotification 테이블
CREATE TABLE application_in_app_notification
(
  in_app_notification_id BIGINT NOT NULL PRIMARY KEY,
  application_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT FK_application_TO_notification
      FOREIGN KEY (in_app_notification_id)
          REFERENCES in_app_notification(in_app_notification_id)
          ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

# 4. MatchInAppNotification 테이블
CREATE TABLE match_in_app_notification
(
    in_app_notification_id BIGINT NOT NULL PRIMARY KEY,
    match_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT FK_match_TO_notification
        FOREIGN KEY (in_app_notification_id)
            REFERENCES in_app_notification(in_app_notification_id)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

# 5. PushNotificationSubscription 테이블
CREATE TABLE push_notification_subscription
(
  web_push_notification_id BIGINT NOT NULL PRIMARY KEY,
  subscriber_id BIGINT NOT NULL,
  token BIGINT NOT NULL UNIQUE,
  device_type ENUM('WEB_PC', 'WEB_IOS', 'WEB_ANDROID') NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;