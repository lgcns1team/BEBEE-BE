# FCM 푸시 알림 테스트 가이드

## 📋 사전 준비

### 1. Firebase 프로젝트 설정

1. [Firebase Console](https://console.firebase.google.com/) 접속
2. 프로젝트 선택 또는 새로 생성
3. **프로젝트 설정** (⚙️ 아이콘) → **일반** 탭
   - 스크롤해서 "내 앱" 섹션에서 **웹 앱 추가** (</> 아이콘)
   - 앱 닉네임 입력 (예: "FCM Test")
   - Firebase SDK 설정 정보 복사 (나중에 사용)

4. **Cloud Messaging** 탭
   - 스크롤해서 **웹 푸시 인증서** 섹션
   - **키 쌍 생성** 버튼 클릭
   - VAPID 키 복사 (나중에 사용)

### 2. 서비스 계정 키 확인

- **프로젝트 설정** → **서비스 계정** 탭
- 이미 생성한 서비스 계정 키가 있는지 확인
- 서버의 `application.yml`에 경로가 올바르게 설정되어 있는지 확인

```yaml
fcm:
  service-account-key-path: classpath:firebase/bebee-firebase-adminsdk.json
```

## 🚀 테스트 실행 방법

### Step 1: 로컬 서버 실행

HTML 파일을 `file://` 프로토콜로 열면 작동하지 않습니다. 로컬 서버가 필요합니다:

#### 방법 1: Python 사용
```bash
cd /Users/hyunwon/Desktop/study/project/bebee/notification-service/src/test/resources
python3 -m http.server 8000
```

#### 방법 2: Node.js 사용
```bash
cd /Users/hyunwon/Desktop/study/project/bebee/notification-service/src/test/resources
npx serve .
```

#### 방법 3: VS Code Live Server 확장 사용
- VS Code에서 `fcm-token-test.html` 파일 우클릭
- "Open with Live Server" 선택

### Step 2: 브라우저에서 열기

```
http://localhost:8000/fcm-token-test.html
```

### Step 3: Firebase 설정 입력

1. Firebase Console에서 복사한 정보를 입력:
   - **API Key**: `AIzaSy...`
   - **Auth Domain**: `your-project.firebaseapp.com`
   - **Project ID**: `your-project-id`
   - **Storage Bucket**: `your-project.appspot.com`
   - **Messaging Sender ID**: `1234567890`
   - **App ID**: `1:1234567890:web:abcdef`
   - **VAPID Key**: `BN...` (Cloud Messaging 탭에서 복사)

2. **"Firebase 초기화"** 버튼 클릭

### Step 4: FCM 토큰 생성

1. **"알림 권한 요청 및 토큰 생성"** 버튼 클릭
2. 브라우저 알림 권한 허용
3. 생성된 토큰 확인 (자동으로 화면에 표시됨)

### Step 5: 서버에 토큰 등록

#### 방법 A: HTML 페이지에서 직접 등록

1. 서버 URL 확인: `http://localhost:8080` (기본값)
2. Member ID 입력: `1` (테스트용)
3. Device Type 선택: `WEB_PC`
4. **"서버에 토큰 등록"** 버튼 클릭

#### 방법 B: Postman/cURL 사용

생성된 토큰을 복사해서 직접 API 호출:

```bash
curl -X POST 'http://localhost:8080/api/notifications/fcm/register?memberId=1' \
  -H 'Content-Type: application/json' \
  -d '{
    "token": "여기에_복사한_토큰_붙여넣기",
    "deviceType": "WEB_PC"
  }'
```

### Step 6: 푸시 알림 전송 테스트

서버에서 알림을 전송하는 API를 호출하거나, 백엔드 로직을 직접 트리거:

```bash
# 예시: 채팅 메시지 전송 시 자동으로 푸시 알림 발송
curl -X POST 'http://localhost:8080/api/chat/send' \
  -H 'Content-Type: application/json' \
  -d '{
    "chatroomId": 123,
    "senderId": 2,
    "content": "안녕하세요!"
  }'
```

또는 직접 UseCase를 호출하는 테스트 API가 있다면:

```bash
curl -X POST 'http://localhost:8080/api/notifications/push/send' \
  -H 'Content-Type: application/json' \
  -d '{
    "senderId": 2,
    "receiverId": 1,
    "notificationType": "CHAT",
    "chatroomId": 123,
    "messagePreview": "안녕하세요!"
  }'
```

## 🔍 확인 사항

### 1. 토큰이 생성되지 않는 경우

- 브라우저 알림 권한이 허용되었는지 확인
- 브라우저 콘솔에서 오류 메시지 확인
- Firebase 설정 정보가 올바른지 확인
- VAPID 키가 올바른지 확인

### 2. 서버 등록이 실패하는 경우

- 서버가 실행 중인지 확인
- CORS 설정 확인 (서버에서 `http://localhost:8000` 허용 필요)
- API 엔드포인트 경로 확인

### 3. 푸시 알림이 오지 않는 경우

- 서버 로그에서 `FcmPushNotificationClient` 로그 확인
- Firebase Console에서 서비스 계정 키 권한 확인
- 토큰이 DB에 올바르게 저장되었는지 확인

```sql
SELECT * FROM push_notification_subscription WHERE subscriber_id = 1;
```

## 📱 브라우저별 주의사항

- **Chrome/Edge**: 정상 작동
- **Firefox**: 정상 작동
- **Safari**: FCM 지원 제한적 (iOS Safari는 미지원)
- **시크릿 모드**: 토큰이 매번 새로 생성됨

## 🎯 다음 단계

1. 실제 프론트엔드 애플리케이션에 FCM SDK 통합
2. 사용자 로그인 시 자동으로 토큰 등록
3. 알림 전송 로직을 비즈니스 로직에 통합 (채팅, 매칭, 신청 등)
4. 알림 클릭 시 해당 페이지로 이동하는 딥링크 구현

## 🐛 트러블슈팅

### Service Worker 관련 오류

브라우저 콘솔에서 Service Worker 오류가 발생하는 경우:

1. 개발자 도구 (F12) → Application → Service Workers
2. 등록된 Service Worker "Unregister"
3. 페이지 새로고침

### 토큰 저장 관련

HTML 페이지는 입력한 Firebase 설정을 LocalStorage에 저장합니다.
설정을 초기화하려면:

```javascript
// 브라우저 콘솔에서 실행
localStorage.clear();
```

## 📚 참고 자료

- [Firebase Cloud Messaging 문서](https://firebase.google.com/docs/cloud-messaging)
- [웹 앱에서 메시지 수신](https://firebase.google.com/docs/cloud-messaging/js/receive)
- [FCM 아키텍처 개요](https://firebase.google.com/docs/cloud-messaging/fcm-architecture)