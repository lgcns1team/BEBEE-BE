# ========================================
# Bebee 개발 환경 MySQL 초기 데이터베이스 생성
# ========================================
#
# 이 스크립트는 Docker 컨테이너가 처음 시작될 때 자동 실행됩니다.
# Bebee 서비스에 필요한 모든 마이크로 서비스별 데이터베이스를 생성합니다.
#
# 설정 값:
# - CHARACTER SET utf8mb4: 이모지 및 다국어 지원을 위한 4바이트 유니코드 설정
# - COLLATE utf8mb4_unicode_ci: 대소문자 비구분(Case-Insensitive) 정렬 방식 설정

CREATE USER IF NOT EXISTS 'bebee'@'%'
    IDENTIFIED BY 'bebee1234';


# 1. bebee_member: 회원 관리 서비스 DB
CREATE DATABASE IF NOT EXISTS bebee_member
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

# 2. bebee_match: 매칭 로직 서비스 DB
CREATE DATABASE IF NOT EXISTS bebee_match
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

# 3. bebee_chat: 채팅 서비스 DB
CREATE DATABASE IF NOT EXISTS bebee_chat
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

# 4. bebee_notification: 알림/푸시 서비스 DB
CREATE DATABASE IF NOT EXISTS bebee_notification
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

# 5. bebee_payment: 결제 및 정산 서비스 DB
CREATE DATABASE IF NOT EXISTS bebee_payment
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

# 생성 확인 쿼리
# 모든 'bebee_'로 시작하는 데이터베이스 목록을 표시하여 생성을 확인합니다.
SHOW DATABASES LIKE 'bebee_%';