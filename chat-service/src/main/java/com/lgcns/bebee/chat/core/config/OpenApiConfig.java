package com.lgcns.bebee.chat.core.config;

import com.lgcns.bebee.chat.core.properties.SpringdocApiProperties;
import com.lgcns.bebee.common.config.BaseOpenApiConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig extends BaseOpenApiConfig {
    private final SpringdocApiProperties springdocApiProperties;

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(createApiInfo(
                        "Bebee Chat Service API",
                        "채팅 서비스 REST API 문서",
                        "v1.0.0"
                ))
                .components(openApiComponents())
                .servers(List.of(
                        new Server()
                                .url(springdocApiProperties.host())
                                .description(springdocApiProperties.desc())
                ));
    }
}
