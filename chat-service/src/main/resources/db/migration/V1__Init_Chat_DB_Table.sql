SET FOREIGN_KEY_CHECKS = 0;

-- DROP TABLES (존재한다면 먼저 삭제)
DROP TABLE IF EXISTS chat_room;
DROP TABLE IF EXISTS chat_room_help_category;

-- Create Table

-- 1. Chatroom 테이블
CREATE TABLE chatroom(
    chatroom_id BIGINT NOT NULL PRIMARY KEY,
    member1_id BIGINT NOT NULL,
    member2_id BIGINT NOT NULL,
    title VARCHAR(50) NULL, # 원래 NULL 아님, 임시로 NULL로 선언

    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_chatroom_members UNIQUE (member1_id, member2_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 2. Chatroom Help Category 테이블
CREATE TABLE chatroom_help_category(
    chat_room_id BIGINT NOT NULL,
    help_category_id BIGINT NOT NULL,

    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `PK_CHATROOM_HELP_CATEGORY` PRIMARY KEY (`chat_room_id`, `help_category_id`),
    CONSTRAINT `FK_chatroom_help_category_TO_chatroom` FOREIGN KEY (`help_category_id`) REFERENCES `chatroom` (`chatroom_id`) #,
    # CONSTRAINT `FK_chatroom_help_category_TO_chat_help_category_sync` FOREIGN KEY (`help_category_id`) REFERENCES `chat_help_category_sync` (`help_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;