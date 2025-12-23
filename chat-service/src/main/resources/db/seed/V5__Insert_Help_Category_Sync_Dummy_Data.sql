-- ========================================
-- HelpCategorySync 더미 데이터
-- ========================================
-- 개발 및 테스트 환경에서 사용할 도움 카테고리 동기화 데이터


-- 카테고리 1: 외출동행
INSERT INTO help_category_sync (help_category_id, name, created_at, updated_at)
VALUES (
    1,
    '외출동행',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    updated_at = NOW();

-- 카테고리 2: 방문목욕
INSERT INTO help_category_sync (help_category_id, name, created_at, updated_at)
VALUES (
    2,
    '방문목욕',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    updated_at = NOW();

-- 카테고리 3: 방문간호
INSERT INTO help_category_sync (help_category_id, name, created_at, updated_at)
VALUES (
    3,
    '방문간호',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    updated_at = NOW();

-- 카테고리 4: 가사지원
INSERT INTO help_category_sync (help_category_id, name, created_at, updated_at)
VALUES (
    4,
    '가사지원',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    updated_at = NOW();

-- 카테고리 5: 식사도움
INSERT INTO help_category_sync (help_category_id, name, created_at, updated_at)
VALUES (
    5,
    '식사도움',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    updated_at = NOW();

-- 카테고리 6: 학습지원
INSERT INTO help_category_sync (help_category_id, name, created_at, updated_at)
VALUES (
    6,
    '학습지원',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    updated_at = NOW();

-- 카테고리 7: 정서적 지원
INSERT INTO help_category_sync (help_category_id, name, created_at, updated_at)
VALUES (
    7,
    '정서적 지원',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    updated_at = NOW();

-- 카테고리 8: 기타생활지원
INSERT INTO help_category_sync (help_category_id, name, created_at, updated_at)
VALUES (
    8,
    '기타생활지원',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    updated_at = NOW();