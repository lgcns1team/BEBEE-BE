package com.lgcns.bebee.chat.core.config;

import com.lgcns.bebee.chat.core.properties.RedisProperties;
import com.lgcns.bebee.chat.infrastructure.redis.RedisSubscriber;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public RedisClient redisClient(){
        RedisURI redisURI = RedisURI.Builder.redis(redisProperties.host())
                .withPort(redisProperties.port())
                .withAuthentication(redisProperties.username(), redisProperties.password())
                .build();
        return RedisClient.create(redisURI);
    }

    @Bean
    public StatefulRedisPubSubConnection<String, String> redisPubSubConnection(
            RedisClient redisClient,
            RedisSubscriber redisSubscriber
    ) {
        StatefulRedisPubSubConnection<String, String> pubSubConnection = redisClient.connectPubSub();
        pubSubConnection.addListener(redisSubscriber);
        return pubSubConnection;
    }

    @Bean
    public RedisPubSubAsyncCommands<String, String> redisConnection(
            StatefulRedisPubSubConnection<String, String> pubSubConnection
    ) {
        return pubSubConnection.async();
    }
}
