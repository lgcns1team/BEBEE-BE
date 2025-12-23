-- ========================================
-- MemberSync 더미 데이터
-- ========================================
-- 개발 및 테스트 환경에서 사용할 회원 동기화 데이터

-- 회원 111
INSERT INTO member_sync (member_id, nickname, profile_image_url, sweetness, created_at, updated_at)
VALUES (
    100,
    '회원111',
    'https://example.com/profiles/member100.jpg',
    85.5,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    updated_at = NOW();

-- 회원 222
INSERT INTO member_sync (member_id, nickname, profile_image_url, sweetness, created_at, updated_at)
VALUES (
    200,
    '회원222',
    'https://example.com/profiles/member200.jpg',
    92.0,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    updated_at = NOW();

