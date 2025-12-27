package com.lgcns.bebee.member.infrastructure.ocr;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * OCR 서비스 WebClient 설정
 */
@Configuration
@RequiredArgsConstructor
public class OcrConfig {
    
    private final OcrProperties properties;
    
    @Bean
    public WebClient ocrWebClient() {
        return WebClient.builder()
            .baseUrl(properties.getBaseUrl())
            .clientConnector(new org.springframework.http.client.reactive.ReactorClientHttpConnector(
                HttpClient.create()
                    .responseTimeout(Duration.ofMillis(properties.getTimeout()))
            ))
            .build();
    }
}
