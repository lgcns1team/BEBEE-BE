SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE chat_in_app_notification DROP FOREIGN KEY FK_chat_TO_notification;
ALTER TABLE application_in_app_notification DROP FOREIGN KEY FK_application_TO_notification;
ALTER TABLE match_in_app_notification DROP FOREIGN KEY FK_match_TO_notification;

RENAME TABLE in_app_notification TO app_notification;

RENAME TABLE chat_in_app_notification TO chat_app_notification;
RENAME TABLE application_in_app_notification TO application_app_notification;
RENAME TABLE match_in_app_notification TO match_app_notification;

ALTER TABLE chat_app_notification
    CHANGE COLUMN in_app_notification_id app_notification_id BIGINT NOT NULL;

ALTER TABLE application_app_notification
    CHANGE COLUMN in_app_notification_id app_notification_id BIGINT NOT NULL;

ALTER TABLE match_app_notification
    CHANGE COLUMN in_app_notification_id app_notification_id BIGINT NOT NULL;

ALTER TABLE app_notification
    CHANGE COLUMN in_app_notification_id app_notification_id BIGINT NOT NULL;

ALTER TABLE chat_app_notification
    ADD CONSTRAINT FK_chat_TO_notification
        FOREIGN KEY (app_notification_id)
            REFERENCES app_notification(app_notification_id)
            ON DELETE CASCADE;

ALTER TABLE application_app_notification
    ADD CONSTRAINT FK_application_TO_notification
        FOREIGN KEY (app_notification_id)
            REFERENCES app_notification(app_notification_id)
            ON DELETE CASCADE;

ALTER TABLE match_app_notification
    ADD CONSTRAINT FK_match_TO_notification
        FOREIGN KEY (app_notification_id)
            REFERENCES app_notification(app_notification_id)
            ON DELETE CASCADE;

SET FOREIGN_KEY_CHECKS = 1;