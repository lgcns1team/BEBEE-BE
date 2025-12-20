// Firebase Cloud Messaging Service Worker

// Firebase SDK 임포트
importScripts('https://www.gstatic.com/firebasejs/10.7.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.7.0/firebase-messaging-compat.js');

// Firebase 설정은 HTML에서 전달받기 때문에 여기서는 초기화하지 않음
// 대신 기본 설정으로 초기화 (HTML에서 전달한 설정으로 덮어씌워짐)

console.log('Service Worker: firebase-messaging-sw.js 로드됨');

// 백그라운드 메시지 처리
self.addEventListener('push', function(event) {
    console.log('Service Worker: Push 이벤트 수신', event);
});

self.addEventListener('notificationclick', function(event) {
    console.log('Service Worker: 알림 클릭됨', event);
    event.notification.close();

    // 알림 클릭 시 앱으로 이동
    event.waitUntil(
        clients.openWindow('/')
    );
});