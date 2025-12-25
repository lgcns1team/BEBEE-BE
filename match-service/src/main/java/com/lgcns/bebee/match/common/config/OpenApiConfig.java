package com.lgcns.bebee.match.common.config;

import com.lgcns.bebee.common.config.BaseOpenApiConfig;
import com.lgcns.bebee.common.properties.SpringdocApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
                        "Bebee Match Service API",
                        "매칭 서비스 REST API 문서",
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
