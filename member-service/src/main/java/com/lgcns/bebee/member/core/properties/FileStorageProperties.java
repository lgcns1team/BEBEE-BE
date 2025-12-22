package com.lgcns.bebee.member.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 파일 저장소 설정 Properties
 * application.yml의 file.* 속성을 매핑
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    /**
     * 파일 업로드 디렉토리
     */
    private String uploadDir = "uploads";

    /**
     * 파일 접근 기본 URL
     */
    private String baseUrl = "http://localhost:8080/files";
}

