SET FOREIGN_KEY_CHECKS = 0;

-- DROP TABLES (존재한다면 먼저 삭제)
DROP TABLE IF EXISTS `help_request_post`;
DROP TABLE IF EXISTS `match`;
DROP TABLE IF EXISTS `agreement`;
DROP TABLE IF EXISTS `engagement`;
DROP TABLE IF EXISTS `post_engagement_time_day`;
DROP TABLE IF EXISTS `post_engagement_time_term`;
DROP TABLE IF EXISTS `agreement_engagement_time_day`;
DROP TABLE IF EXISTS `agreement_engagement_time_schedule`;
DROP TABLE IF EXISTS `agreement_engagement_time_term`;
DROP TABLE IF EXISTS `post_help_category`;
DROP TABLE IF EXISTS `agreement_help_category`;
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `application`;
DROP TABLE IF EXISTS `post_image`;

-- CREATE TABLES

-- 1. HelpRequestPost 테이블
CREATE TABLE `help_request_post`
(
    `post_id`         BIGINT        NOT NULL PRIMARY KEY,
    `member_id`       BIGINT        NOT NULL,
    `title`           VARCHAR(50)   NOT NULL,
    `type`            ENUM('DAY','TERM') NOT NULL,
    `unit_honey`      INT           NOT NULL,
    `total_honey`     INT           NOT NULL,
    `region`          VARCHAR(30)   NOT NULL,
    `status`          ENUM('NON_MATCHED','PROCEEDING','MATCHED') NOT NULL DEFAULT 'NON_MATCHED',
    `legaldong_code`  VARCHAR(10)   NOT NULL,
    `latitude`        DECIMAL(10,7) NOT NULL,
    `longitude`       DECIMAL(10,7) NOT NULL,
    `applicant_count` INT           NULL DEFAULT 0,
    `content`         VARCHAR(1000) NULL,
    `created_at`      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 2. Match 테이블
CREATE TABLE `match`
(
    `match_id`     BIGINT       NOT NULL PRIMARY KEY,
    `helper_id`    BIGINT       NOT NULL,
    `disabled_id`  BIGINT       NOT NULL,
    `post_id`      BIGINT       NOT NULL,
    `title`        VARCHAR(100) NOT NULL,
    `chat_room_id` BIGINT       NOT NULL,
    `agreement_id` BIGINT       NOT NULL UNIQUE,
    `created_at`   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_match_TO_post`
        FOREIGN KEY(`post_id`)
            REFERENCES `help_request_post`(`post_id`)
            ON DELETE RESTRICT,
    CONSTRAINT `UQ_post_id` UNIQUE (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 3. Agreement 테이블
CREATE TABLE `agreement`
(
    `agreement_id`      BIGINT      NOT NULL PRIMARY KEY,
    `post_id`           BIGINT      NOT NULL,
    `helper_id`         BIGINT      NOT NULL,
    `disabled_id`       BIGINT      NOT NULL,
    `unit_honey`        INT         NOT NULL,
    `total_honey`       INT         NOT NULL,
    `region`            VARCHAR(50) NOT NULL,
    `type`              ENUM('DAY','TERM') NOT NULL,
    `confirmation_date` DATE        NOT NULL,
    `is_day_complete`   BOOLEAN     NULL DEFAULT FALSE,
    `is_term_complete`  BOOLEAN     NULL DEFAULT FALSE,
    `status`            ENUM('BEFORE', 'REFUSED', 'CONFIRMED') NOT NULL DEFAULT 'BEFORE',
    `is_volunteer`      BOOLEAN     NOT NULL,
    `created_at`        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 4. Engagement 테이블
CREATE TABLE `engagement`
(
    `engagement_id`     BIGINT   NOT NULL PRIMARY KEY,
    `agreement_id`      BIGINT   NOT NULL,
    `type`              ENUM('DAY', 'TERM') NOT NULL,
    `is_disabled_check` BOOLEAN  NULL DEFAULT FALSE,
    `is_helper_check`   BOOLEAN  NULL DEFAULT FALSE,
    `count`             BIGINT   NULL DEFAULT 0,
    `created_at`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_engagement_TO_agreement`
        FOREIGN KEY (`agreement_id`)
            REFERENCES `agreement` (`agreement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- 5. ActiveTime_Day 테이블
CREATE TABLE `post_engagement_time_day`
(
    `post_id`         BIGINT   NOT NULL PRIMARY KEY,
    `engagement_date` DATE     NOT NULL,
    `engagement_time` TIME     NOT NULL,
    `created_at`      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_post_engagement_time_day_TO_help_request_post`
        FOREIGN KEY (`post_id`)
            REFERENCES `help_request_post` (`post_id`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 6. ActiveTime_Term 테이블
CREATE TABLE `post_engagement_time_term`
(
    `post_id`         BIGINT   NOT NULL PRIMARY KEY,
    `start_date`      DATE     NOT NULL,
    `end_date`        DATE     NOT NULL,
    `engagement_time` JSON     NOT NULL,
    `created_at`      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_post_engagement_time_term_TO_help_request_post`
        FOREIGN KEY (`post_id`)
            REFERENCES `help_request_post` (`post_id`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 7. AgreementPeriod 테이블 (게시글 날짜/기간 정보)
CREATE TABLE agreement_period
(
    `agreement_period_id` BIGINT   NOT NULL PRIMARY KEY,
    `agreement_id`        BIGINT   NOT NULL,
    `start_date`     DATE     NOT NULL,
    `end_date`       DATE     NOT NULL,
    `created_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_agreement_period_TO_agreement`
        FOREIGN KEY (`agreement_id`)
            REFERENCES agreement (`agreement_id`)
            ON DELETE CASCADE,
    CONSTRAINT `UQ_post_period_agreement_id` UNIQUE (`agreement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 8. AgreementSchedule 테이블 (게시글 요일별 스케줄 정보)
CREATE TABLE agreement_schedule
(
    `agreement_schedule_id` BIGINT   NOT NULL PRIMARY KEY,
    `agreement_id`          BIGINT   NOT NULL,
    `day_of_week`      ENUM('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') NOT NULL,
    `start_time`       TIME     NOT NULL,
    `end_time`         TIME     NOT NULL,
    `created_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_agreement_schedule_TO_agreement`
        FOREIGN KEY (`agreement_id`)
            REFERENCES agreement (`agreement_id`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 10. Post_HelpCategory 테이블
CREATE TABLE `post_help_category`
(
    `post_id`    BIGINT   NOT NULL,
    `help_category_id`    BIGINT   NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`post_id`, `help_category_id`),
    CONSTRAINT `FK_post_help_category_TO_help_request_post`
        FOREIGN KEY (`post_id`)
            REFERENCES `help_request_post` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 11. Agreement_HelpCategory 테이블
CREATE TABLE `agreement_help_category`
(
    `agreement_id` BIGINT   NOT NULL,
    `help_category_id`      BIGINT   NOT NULL,
    `category_name`         VARCHAR(10) NOT NULL,
    `created_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`agreement_id`, `help_category_id`),
    CONSTRAINT `FK_agreement_help_category_TO_agreement`
        FOREIGN KEY (`agreement_id`)
            REFERENCES `agreement` (`agreement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 12. Review 테이블
CREATE TABLE `review`
(
    `review_id`     BIGINT   NOT NULL PRIMARY KEY,
    `engagement_id` BIGINT   NOT NULL,
    `rating`        INT      NOT NULL,
    `created_at`    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_review_TO_engagement`
        FOREIGN KEY (`engagement_id`)
            REFERENCES `engagement` (`engagement_id`),
    CHECK (`rating` BETWEEN 1 AND 4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 13. Application 테이블
CREATE TABLE `application`
(
    `application_id` BIGINT   NOT NULL PRIMARY KEY,
    `post_id`        BIGINT   NOT NULL,
    `applicant_id`   BIGINT   NOT NULL,
    `is_volunteer`   BOOLEAN  NOT NULL,
    `created_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_application_TO_help_request_post`
        FOREIGN KEY (`post_id`)
            REFERENCES `help_request_post` (`post_id`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 14. PostImage 테이블
CREATE TABLE `post_image`
(
    `image_id`   BIGINT       NOT NULL PRIMARY KEY,
    `image_url`  VARCHAR(255) NOT NULL,
    `post_id`    BIGINT       NOT NULL,
    `sequence`   INT          NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_post_image_TO_help_request_post`
        FOREIGN KEY (`post_id`)
            REFERENCES `help_request_post` (`post_id`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 15. Member Sync 테이블
CREATE TABLE `match_member_sync`
(
    `member_id`         BIGINT          NOT NULL PRIMARY KEY,
    `nickname`          VARCHAR(10)     NOT NULL,
    `gender`            ENUM('MALE','FEMALE','NONE') NOT NULL DEFAULT 'NONE',
    `birth_date`        DATE            NOT NULL,
    `role`              ENUM('ADMIN','DISABLED','HELPER') NOT NULL,
    `status`            VARCHAR(20)     NOT NULL,
    `profile_image_url` VARCHAR(255)    NULL,
    `address_road`      VARCHAR(255)    NULL,
    `latitude`          DECIMAL(10,7)   NOT NULL,
    `longitude`         DECIMAL(10,7)   NOT NULL,
    `district_code`     CHAR(10)        NOT NULL,
    `created_at`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `UQ_MEMBER_NICK`  UNIQUE (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;