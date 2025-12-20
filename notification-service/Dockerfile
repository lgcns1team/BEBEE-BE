# ====================================================
# 1. Builder Stage
# ====================================================
FROM amazoncorretto:17-alpine AS builder
WORKDIR /app

# [중요] 루트에 있는 Gradle 래퍼와 설정 파일들을 복사합니다.
# Dockerfile이 하위 폴더에 있어도, 빌드 컨텍스트가 루트라면 루트 경로(./)에서 가져옵니다.
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# [중요] 의존성(라이브러리) 캐싱을 위해 필요한 모듈을 먼저 복사합니다.
# 1. 공통 모듈 (chat-service가 의존하는 경우 필수)
COPY common ./common

# 2. 빌드할 타겟 서비스 모듈 (현재 서비스)
COPY notification-service ./notification-service

# 실행 권한 부여
RUN chmod +x ./gradlew

# [중요] 루트에서 래퍼를 실행하되, 특정 모듈(:chat-service)만 bootJar로 빌드합니다.
# -x test: 테스트 제외
RUN ./gradlew :notification-service:bootJar -x test --no-daemon

# ====================================================
# 2. Runtime Stage
# ====================================================
FROM amazoncorretto:17-alpine
WORKDIR /app

# 빌드된 Jar 파일 복사
# 경로는 /app/{모듈명}/build/libs/ 입니다.
COPY --from=builder /app/chat-service/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]