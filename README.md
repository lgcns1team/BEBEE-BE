
## 🚀 시작하기

### 1. 환경변수 설정

```shell
# env.example 파일을 .env로 복사
cp env.example .env

# .env 파일에서 실제 값으로 수정
# 특히 JWT 시크릿 키는 반드시 변경해야 합니다!
```

### 2. 인프라 서비스 실행
```shell
# Docker Compose로 MySQL, Redis 실행
docker-compose up -d

# 서비스 상태 확인
docker-compose ps

# 특정 서비스 중지 및 삭제
docker-compose rm -sf ${서비스명}  # ex) 서비스 명: localstack
```

## 📁 프로젝트 구조
```shell
├── common
│   ├── build
│   ├── build.gradle
│   └── src
├── chat-service
│   ├── build.gradle
│   └── src
├── match-service
│   ├── build.gradle
│   └── src
├── member-service
│   ├── build.gradle
│   └── src
├── notification-service
│   ├── build.gradle
│   └── src
├── payment-service
│   ├── build.gradle
│   └── src
├── docker
│   └── mysql
├── build.gradle
├── settings.gradle
├── docker-compose.yaml
└── env.example
```
