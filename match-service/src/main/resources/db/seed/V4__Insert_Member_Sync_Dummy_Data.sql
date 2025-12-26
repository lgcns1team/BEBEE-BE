-- ========================================
-- MemberSync 더미 데이터
-- ========================================
-- 개발 및 테스트 환경에서 사용할 회원 동기화 데이터 (총 10명)

-- 회원 1: 관리자
INSERT INTO member_sync (member_id, nickname, gender, role, latitude, longitude, profile_image_url, sweetness, legal_dong_code, created_at, updated_at)
VALUES (
    100,
    '김철수',
    'MALE',
    'DISABLED',
    37.5012,
    127.0396,
    'https://example.com/profiles/admin.jpg',
    100.00,
    '1168010500',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 2: 장애인 (도움이 필요한 분)
INSERT INTO member_sync (member_id, nickname, gender, role, latitude, longitude, profile_image_url, sweetness, legal_dong_code, created_at, updated_at)
VALUES (
    200,
    '김민수',
    'MALE',
    'DISABLED',
    37.4837,
    127.0324,
    'https://example.com/profiles/member200.jpg',
    85.5,
    '1165010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 3: 장애인 (도움이 필요한 분)
INSERT INTO member_sync (member_id, nickname, gender, role, latitude, longitude, profile_image_url, sweetness, legal_dong_code, created_at, updated_at)
VALUES (
    300,
    '이영희',
    'FEMALE',
    'DISABLED',
    37.5133,
    127.1000,
    'https://example.com/profiles/member300.jpg',
    92.0,
    '1171010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 4: 장애인 (도움이 필요한 분)
INSERT INTO member_sync (member_id, nickname, gender, role, latitude, longitude, profile_image_url, sweetness, legal_dong_code, created_at, updated_at)
VALUES (
    400,
    '박민수',
    'MALE',
    'DISABLED',
    37.5388,
    127.1236,
    'https://example.com/profiles/member400.jpg',
    78.3,
    '1174010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 5: 장애인 (도움이 필요한 분)
INSERT INTO member_sync (member_id, nickname, gender, role, latitude, longitude, profile_image_url, sweetness, legal_dong_code, created_at, updated_at)
VALUES (
    500,
    '최은정',
    'FEMALE',
    'DISABLED',
    37.5443,
    127.0557,
    'https://example.com/profiles/member500.jpg',
    88.7,
    '1120010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 6: 장애인 (도움이 필요한 분)
INSERT INTO member_sync (member_id, nickname, gender, role, latitude, longitude, profile_image_url, sweetness, legal_dong_code, created_at, updated_at)
VALUES (
    600,
    '정수진',
    'NONE',
    'DISABLED',
    37.5833,
    127.0913,
    'https://example.com/profiles/member600.jpg',
    81.2,
    '1126010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 7: 도우미
INSERT INTO member_sync (member_id, nickname, gender, role, latitude, longitude, profile_image_url, sweetness, legal_dong_code, created_at, updated_at)
VALUES (
    700,
    '강지훈',
    'MALE',
    'HELPER',
    37.5894,
    127.0582,
    'https://example.com/profiles/member700.jpg',
    95.5,
    '1123010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 8: 도우미
INSERT INTO member_sync (member_id, nickname, gender, role, latitude, longitude, profile_image_url, sweetness, legal_dong_code, created_at, updated_at)
VALUES (
    800,
    '윤서연',
    'FEMALE',
    'HELPER',
    37.6106,
    127.0099,
    'https://example.com/profiles/member800.jpg',
    97.8,
    '1117010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 9: 도우미
INSERT INTO member_sync (member_id, nickname, gender, role, latitude, longitude, profile_image_url, sweetness, legal_dong_code, created_at, updated_at)
VALUES (
    900,
    '임동현',
    'MALE',
    'HELPER',
    37.6388,
    127.0255,
    'https://example.com/profiles/member900.jpg',
    93.2,
    '1132010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 10: 도우미
INSERT INTO member_sync (member_id, nickname, gender, role, latitude, longitude, profile_image_url, sweetness, legal_dong_code, created_at, updated_at)
VALUES (
    1000,
    '한미래',
    'FEMALE',
    'HELPER',
    37.6500,
    127.0322,
    'https://example.com/profiles/member1000.jpg',
    96.4,
    '1135010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();