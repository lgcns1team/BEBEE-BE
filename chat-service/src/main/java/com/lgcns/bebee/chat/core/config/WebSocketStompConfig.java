package com.lgcns.bebee.chat.core.config;

import com.lgcns.bebee.chat.core.properties.WebSocketStompProperties;
import com.lgcns.bebee.chat.core.utils.StompDestinationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.lgcns.bebee.chat.core.utils.StompDestinationUtils.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketStompProperties properties;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] origins = properties.allowedOriginPatterns().toArray(String[]::new);

        registry.addEndpoint(ENDPOINT)
                .setAllowedOriginPatterns(origins)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(PUBLISH_PREFIX)
                .enableSimpleBroker(SUBSCRIBE_PREFIX);
    }
}
