package com.lgcns.bebee.notification.application.client;

import java.util.Map;

public interface PushNotificationClient {
    void sendMessage(String token, String title, String body, Map<String, String> data);
}
