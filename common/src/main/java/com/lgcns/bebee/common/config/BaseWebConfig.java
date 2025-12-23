package com.lgcns.bebee.common.config;

import com.lgcns.bebee.common.properties.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
public class BaseWebConfig implements WebMvcConfigurer {
    private final CorsProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] allowedOriginPatterns = corsProperties.allowedOriginPatterns().toArray(String[]::new);
        String[] allowedMethods = corsProperties.allowedMethods().toArray(String[]::new);
        String[] allowedHeaders = corsProperties.allowedHeaders().toArray(String[]::new);
        String[] exposedHeaders = corsProperties.exposedHeaders().toArray(String[]::new);

        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOriginPatterns)
                .allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders)
                .exposedHeaders(exposedHeaders)
                .allowCredentials(corsProperties.allowCredentials())
                .maxAge(corsProperties.maxAge());
    }
}
