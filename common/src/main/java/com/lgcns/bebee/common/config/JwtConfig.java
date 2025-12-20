package com.lgcns.bebee.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT 설정을 위한 Configuration 클래스
 * @ConfigurationProperties가 제대로 동작하도록 @EnableConfigurationProperties를 추가
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {
}
