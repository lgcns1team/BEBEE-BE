package com.lgcns.bebee.chat.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(
    String host,
    int port,
    String username,
    String password
) {
}
