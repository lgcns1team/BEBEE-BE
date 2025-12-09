package com.lgcns.bebee.chat.core.config;

import com.lgcns.bebee.chat.core.properties.RedisProperties;
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
    public RedisPubSubAsyncCommands<String, String> redisConnection(RedisClient redisClient){
        StatefulRedisPubSubConnection<String, String> pubSubConnection = redisClient.connectPubSub();
        return pubSubConnection.async();
    }
}
