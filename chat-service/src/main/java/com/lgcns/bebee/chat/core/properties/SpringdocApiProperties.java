package com.lgcns.bebee.chat.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "springdoc.api")
public record SpringdocApiProperties(
        String host,
        String desc
) {
}
