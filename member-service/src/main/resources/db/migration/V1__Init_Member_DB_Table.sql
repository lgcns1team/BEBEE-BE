SET FOREIGN_KEY_CHECKS = 0;

# DROP TABLES
DROP TABLE IF EXISTS `document_verification`;
DROP TABLE IF EXISTS `member_disability_category`;
DROP TABLE IF EXISTS `member_help_category`;
DROP TABLE IF EXISTS `helper_career`;
DROP TABLE IF EXISTS `social_login`;
DROP TABLE IF EXISTS `document`;
DROP TABLE IF EXISTS `disability_category`;
DROP TABLE IF EXISTS `help_category`;
DROP TABLE IF EXISTS `member`;

# CREATE TABLES
-- 1. MEMBER
CREATE TABLE `member` (
    `member_id`        BIGINT       NOT NULL PRIMARY KEY,
    `email`            VARCHAR(30)  NOT NULL,
    `password`         VARCHAR(255) NOT NULL,
    `name`             VARCHAR(50)  NOT NULL,
    `nickname`         VARCHAR(10)  NOT NULL,
    `birth_date`       DATE         NOT NULL,
    `gender`           ENUM('MALE','FEMALE','NONE') NOT NULL DEFAULT 'NONE',
    `phone_number`     VARCHAR(20)  NOT NULL,
    `role`             ENUM('ADMIN','DISABLED','HELPER') NOT NULL,
    `status`           VARCHAR(20)  NOT NULL,
    `profile_image_url` VARCHAR(255) NULL,
    `address_road`     VARCHAR(255) NULL,
    `address_number`   VARCHAR(10)  NOT NULL,
    `introduction`     VARCHAR(255) NULL,
    `latitude`         DECIMAL(10,7) NOT NULL,
    `longitude`        DECIMAL(10,7) NOT NULL,
    `district_code`    CHAR(10)     NOT NULL,
    `sweetness`        DECIMAL(5,2) NOT NULL DEFAULT 40.00,
    `created_at`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `UQ_MEMBER_EMAIL` UNIQUE (`email`),
    CONSTRAINT `UQ_MEMBER_NICK`  UNIQUE (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. DOCUMENT_CLASSIFICATION
CREATE TABLE `document` (
    `document_id` BIGINT  NOT NULL PRIMARY KEY,
    `target_role` VARCHAR(20)  NOT NULL,
    `doc_code`    VARCHAR(30)  NOT NULL,
    `doc_name_ko` VARCHAR(50)  NOT NULL,
    `description` VARCHAR(255) NOT NULL,
    `member_id`   BIGINT       NOT NULL,
    `created_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `FK_document_TO_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
    CONSTRAINT `UQ_DOCUMENT_CLASSIFICATION_CODE` UNIQUE (`doc_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 3. DOCUMENT_VERIFICATION
CREATE TABLE `document_verification` (
    `document_verification_id`   BIGINT  NOT NULL PRIMARY KEY,
    `file_url`      VARCHAR(255) NOT NULL,
    `exif_score`    INT          NULL,
    `ocr_score`     INT          NULL,
    `forgery_score` INT          NULL,
    `status`        VARCHAR(10)  NOT NULL DEFAULT 'PENDING',
    `reason`        VARCHAR(255) NULL,
    `document_id`   BIGINT       NOT NULL,
    `created_at`    DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    DATETIME     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `FK_doc_verification_TO_document` FOREIGN KEY (`document_id`) REFERENCES `document` (`document_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 4. DISABILITY_CATEGORY
CREATE TABLE `disability_category` (
    `disability_category_id` BIGINT      NOT NULL PRIMARY KEY,
    `type`        VARCHAR(10) NOT NULL,
    `created_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `UQ_DISABILITY_CATEGORY_TYPE` UNIQUE (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- 5. MEMBER_DISABILITY_CATEGORY
CREATE TABLE `member_disability_category` (
    `member_id`              BIGINT       NOT NULL,
    `disability_category_id` BIGINT       NOT NULL,
    `level`                  CHAR(1)      NOT NULL,
    `disability_description` VARCHAR(300) NOT NULL,
    `created_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `PK_MEMBER_DISABILITY_INFO` PRIMARY KEY (`member_id`, `disability_category_id`),
    CONSTRAINT `FK_member_disability_category_TO_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
    CONSTRAINT `FK_member_disability_category_TO_disability` FOREIGN KEY (`disability_category_id`) REFERENCES `disability_category` (`disability_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 6. HELP_CATEGORY
CREATE TABLE `help_category` (
    `help_category_id` BIGINT      NOT NULL PRIMARY KEY,
    `name`             VARCHAR(30) NOT NULL,
    `created_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `UQ_HELP_CATEGORY_NAME` UNIQUE (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- 7. MEMBER_HELP_CATEGORY
CREATE TABLE `member_help_category` (
    `help_category_id` BIGINT NOT NULL,
    `member_id`        BIGINT NOT NULL,
    `created_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `PK_MEMBER_HELP_CATEGORY` PRIMARY KEY (`help_category_id`, `member_id`),
    CONSTRAINT `FK_member_help_category_TO_help_category` FOREIGN KEY (`help_category_id`) REFERENCES `help_category` (`help_category_id`),
    CONSTRAINT `FK_member_help_category_TO_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. HELPER_CAREER
CREATE TABLE `helper_career` (
    `helper_career_id`    BIGINT       NOT NULL PRIMARY KEY,
    `type`         ENUM('CAREER','CERT') NOT NULL,
    `title`        VARCHAR(50)  NOT NULL,
    `organization` VARCHAR(30)  NULL,
    `period`       INT          NULL,
    `member_id`    BIGINT       NOT NULL COMMENT 'TSID',
    `created_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `FK_helper_career_TO_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 9. SOCIAL_LOGIN
CREATE TABLE `social_login` (
    `social_login_id`  BIGINT       NOT NULL PRIMARY KEY,
    `provider`         VARCHAR(20)  NOT NULL,
    `provider_user_id` VARCHAR(100) NOT NULL,
    `member_id`        BIGINT       NOT NULL,
    `created_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `UQ_MEMBER_SOCIAL_PROVIDER_USER`
       UNIQUE (`provider`, `provider_user_id`),
    CONSTRAINT `FK_social_login_TO_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
