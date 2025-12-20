-- 문서 검증 테이블에 system_flag 컬럼 추가 (엔티티 DocumentVerification 대응)
-- 컬럼 존재 시 추가 스킵 (MySQL)
SET @col_exists := (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'document_verification'
      AND COLUMN_NAME = 'system_flag'
);

SET @ddl := IF(
    @col_exists = 0,
    'ALTER TABLE document_verification ADD COLUMN system_flag VARCHAR(10) NULL',
    'SELECT 1'
);

PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
