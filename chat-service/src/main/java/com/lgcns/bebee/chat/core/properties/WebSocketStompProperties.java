package com.lgcns.bebee.chat.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "websocket.stomp")
public record WebSocketStompProperties(
    List<String> allowedOriginPatterns
) {
}
