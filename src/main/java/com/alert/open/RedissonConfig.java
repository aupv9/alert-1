package com.alert.open;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Autowired
    private RedisConfig redisConfig;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {

        Config config = new Config();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        config.setCodec(new JsonJacksonCodec(objectMapper));
        config.useSingleServer()
                .setAddress("redis://" + redisConfig.getHost() + ":" + redisConfig.getPort())
                .setDatabase(redisConfig.getDatabase())
                .setConnectionPoolSize(redisConfig.getConnectionPoolSize())
                .setConnectTimeout(redisConfig.getConnectTimeout())
                .setConnectionMinimumIdleSize(3)
                .setRetryAttempts(redisConfig.getRetryAttempts())
        ;
        config.setCodec(new JsonJacksonCodec(objectMapper));
        config.setLockWatchdogTimeout(10000);
        return Redisson.create(config);
    }
}