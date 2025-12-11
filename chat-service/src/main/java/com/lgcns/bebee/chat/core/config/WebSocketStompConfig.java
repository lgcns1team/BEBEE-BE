package com.lgcns.bebee.chat.core.config;

import com.lgcns.bebee.chat.core.properties.WebSocketStompProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
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
    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] origins = properties.allowedOriginPatterns().toArray(String[]::new);

        registry.addEndpoint(ENDPOINT)
                .setAllowedOriginPatterns(origins);
//                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(PUBLISH_PREFIX)
                .enableSimpleBroker(SUBSCRIBE_PREFIX);
    }

    /**
     * 클라이언트로부터 들어오는 메시지 채널에 인터셉터를 등록합니다.
     * WebSocket 인증 처리를 위한 인터셉터가 추가됩니다.
     *
     * @param registration 채널 등록 설정
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketAuthInterceptor);
    }
}
