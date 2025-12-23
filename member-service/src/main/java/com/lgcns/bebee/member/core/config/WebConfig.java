package com.lgcns.bebee.member.core.config;

import com.lgcns.bebee.common.config.BaseWebConfig;
import com.lgcns.bebee.common.properties.CorsProperties;
import com.lgcns.bebee.common.properties.JwtProperties;
import com.lgcns.bebee.common.web.CurrentMemberArgumentResolver;
import com.lgcns.bebee.common.web.JwtAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

/**
 * Web MVC 설정
 */
@Configuration
public class WebConfig extends BaseWebConfig {
    private final JwtProperties jwtProperties;
    private final CurrentMemberArgumentResolver currentMemberArgumentResolver;

    @Autowired
    public WebConfig(CorsProperties corsProperties, JwtProperties jwtProperties, CurrentMemberArgumentResolver currentMemberArgumentResolver) {
        super(corsProperties);
        this.jwtProperties = jwtProperties;
        this.currentMemberArgumentResolver = currentMemberArgumentResolver;
    }

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