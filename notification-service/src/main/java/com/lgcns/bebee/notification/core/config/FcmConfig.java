package com.lgcns.bebee.notification.core.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lgcns.bebee.notification.core.properties.FcmProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class FcmConfig {
    private final FcmProperties fcmProperties;
    private final ResourceLoader resourceLoader;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        InputStream serviceAccountInputStream = loadServiceAccountKey();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountInputStream))
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    private InputStream loadServiceAccountKey() throws IOException {

        // Resource Loader 를 통해 경로 타입을 파악해서 파일을 가져온다.
        Resource resource = resourceLoader.getResource(fcmProperties.serviceAccountKeyPath());

        if (!resource.exists()) {
            throw new FileNotFoundException("FCM Service Account Key file not found: " + fcmProperties.serviceAccountKeyPath());
        }

        return resource.getInputStream();
    }
}
