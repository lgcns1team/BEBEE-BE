package com.lgcns.bebee.chat.core.config;

import com.lgcns.bebee.chat.core.properties.WebSocketStompProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketStompProperties webSocketStompProperties;

    private static final String STOMP_ENDPOINT = "/ws/chat";
    private static final String PUBLISH_PREFIX = "/pub";
    private static final String SUBSCRIBE_PREFIX = "/sub";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] origins = webSocketStompProperties.allowedOriginPatterns().toArray(String[]::new);

        registry.addEndpoint(STOMP_ENDPOINT)
                .setAllowedOriginPatterns(origins)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(PUBLISH_PREFIX)
                .enableSimpleBroker(SUBSCRIBE_PREFIX);
    }
}
