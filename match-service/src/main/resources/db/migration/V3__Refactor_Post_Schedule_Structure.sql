SET FOREIGN_KEY_CHECKS = 0;

-- ========================================
-- PostDay 테이블 삭제 및 PostSchedule 구조 변경
-- ========================================
-- PostDay와 PostPeriod를 PostPeriod 하나로 통합
-- PostSchedule을 OneToOne에서 OneToMany로 변경

-- 1. PostDay 테이블 삭제
DROP TABLE IF EXISTS post_day;

-- 2. PostSchedule 테이블 수정
-- day 컬럼명을 dayOfWeek로 변경하고 NOT NULL로 설정
ALTER TABLE post_schedule
    CHANGE COLUMN `day` `day_of_week` ENUM('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') NOT NULL;

-- start_time과 end_time을 NOT NULL로 변경
ALTER TABLE post_schedule
    MODIFY COLUMN `start_time` TIME NOT NULL,
    MODIFY COLUMN `end_time` TIME NOT NULL;

-- 3. PostPeriod 테이블은 그대로 유지
-- DAY 타입: startDate = endDate (같은 날짜)
-- TERM 타입: startDate != endDate (기간)

SET FOREIGN_KEY_CHECKS = 1;