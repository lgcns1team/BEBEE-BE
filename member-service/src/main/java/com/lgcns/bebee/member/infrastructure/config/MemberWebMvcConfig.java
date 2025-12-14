package com.lgcns.bebee.member.infrastructure.config;

import com.lgcns.bebee.member.infrastructure.security.CurrentMemberArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Member 서비스 WebMvc 설정
 * ArgumentResolver 등록
 */
@Configuration
@RequiredArgsConstructor
public class MemberWebMvcConfig implements WebMvcConfigurer {

    private final CurrentMemberArgumentResolver currentMemberArgumentResolver;

    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentMemberArgumentResolver);
    }
}

