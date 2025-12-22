package com.lgcns.bebee.member.core.config;

import com.lgcns.bebee.common.properties.JwtProperties;
import com.lgcns.bebee.common.web.CurrentMemberArgumentResolver;
import com.lgcns.bebee.common.web.JwtAuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web MVC 설정
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtProperties jwtProperties;
    private final CurrentMemberArgumentResolver currentMemberArgumentResolver;

    @Bean
    public JwtAuthenticationInterceptor jwtAuthenticationInterceptor(){
        return new JwtAuthenticationInterceptor(jwtProperties);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/**",
                        "/error",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentMemberArgumentResolver);
    }
}