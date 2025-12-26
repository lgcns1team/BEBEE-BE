SET FOREIGN_KEY_CHECKS = 0;

-- DROP TABLES (존재한다면 먼저 삭제)
DROP TABLE IF EXISTS member_sync;

-- 1. MemberSync 테이블 (회원 정보 동기화)
CREATE TABLE member_sync (
    member_id BIGINT NOT NULL PRIMARY KEY COMMENT '회원 ID',
    nickname VARCHAR(30) NOT NULL COMMENT '닉네임',
    `gender` ENUM('MALE','FEMALE','NONE') NOT NULL DEFAULT 'NONE',
    `role` ENUM('ADMIN','DISABLED','HELPER') NOT NULL,
    `latitude` DECIMAL(10,7) NOT NULL,
    `longitude` DECIMAL(10,7) NOT NULL,
    profile_image_url VARCHAR(512) NULL COMMENT '프로필 이미지 URL',
    sweetness DECIMAL(5,2) NOT NULL COMMENT '평점 (스위트니스)',
    legal_dong_code VARCHAR(10) NOT NULL COMMENT '법정동 코드',

    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='회원 정보 동기화 테이블';

-- 2. Post 테이블에 MemberSync 외래키 제약조건 추가
ALTER TABLE post
    ADD CONSTRAINT `FK_post_TO_member_sync`
        FOREIGN KEY (member_id)
            REFERENCES member_sync (member_id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE;

SET FOREIGN_KEY_CHECKS = 1;