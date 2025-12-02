# ========================================
# Bebee 개발 환경 MySQL 권한 설정
# ========================================
#
# 이 스크립트는 Docker 컨테이너가 처음 시작될 때 자동 실행됩니다.
# bebee 사용자에게 데이터베이스 에 대한 권한을 설정합니다.

# 사용자 bebee 에 bebee_chat 데이터베이스에 대한 권한 설정
GRANT ALL PRIVILEGES ON bebee_chat.* TO 'bebee'@'%';

# 사용자 bebee 에 bebee_member 데이터베이스에 대한 권한 설정
GRANT ALL PRIVILEGES ON bebee_member.* TO 'bebee'@'%';

# 사용자 bebee 에 bebee_match 데이터베이스에 대한 권한 설정
GRANT ALL PRIVILEGES ON bebee_match.* TO 'bebee'@'%';

# 사용자 bebee 에 bebee_notification 데이터베이스에 대한 권한 설정
GRANT ALL PRIVILEGES ON bebee_notification.* TO 'bebee'@'%';

# 사용자 bebee 에 bebee_payment 데이터베이스에 대한 권한 설정
GRANT ALL PRIVILEGES ON bebee_payment.* TO 'bebee'@'%';