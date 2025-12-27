package com.lgcns.bebee.file.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.cloud.aws")
public class S3Properties {
    private S3Config s3;
    private RegionConfig region;

    @Getter
    @Setter
    public static class S3Config {
        private String bucket;
        private String baseUrl;
        private int presignedUrlExpirationMinutes = 10; // 기본값: 10분
    }

    @Getter
    @Setter
    public static class RegionConfig {
        private String staticRegion;

        // 'static'은 Java 예약어이므로 getter 메서드명을 직접 지정
        public String getStatic() {
            return staticRegion;
        }

        public void setStatic(String staticRegion) {
            this.staticRegion = staticRegion;
        }
    }
}