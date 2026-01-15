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
     * 배포 환경: 게이트웨이를 통해 접근 (https://api.be-bee.link/ocr)
     * 로컬 환경: http://localhost:8086
     */
    private String baseUrl = "https://api.be-bee.link/ocr";

    /**
     * 타임아웃 (밀리초)
     */
    private int timeout = 120000;
}
