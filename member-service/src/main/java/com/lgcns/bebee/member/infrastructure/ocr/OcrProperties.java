package com.lgcns.bebee.member.infrastructure.ocr;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OCR 서비스 설정 Properties
 */
@Component
@ConfigurationProperties(prefix = "ocr")
@Getter
@Setter
public class OcrProperties {
    
    /**
     * OCR 서비스 기본 URL
     */
    private String baseUrl = "http://localhost:8086";
    
    /**
     * 타임아웃 (밀리초)
     */
    private int timeout = 30000;
}
