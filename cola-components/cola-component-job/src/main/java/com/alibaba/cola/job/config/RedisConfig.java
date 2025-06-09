package com.alibaba.cola.job.config;

import com.alibaba.cola.job.repository.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@AutoConfigureAfter(EnableJobConfiguration.class)
public class RedisConfig {
    @Bean("jobRedisTemplate")
    public RedisTemplate<String, Object> jobRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return buildTemplate(redisConnectionFactory, JsonUtil.ObjectMapperFactory.getObjectMapper());
    }

    @Bean("stepRedisTemplate")
    public RedisTemplate<String, Object> stepRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return buildTemplate(redisConnectionFactory, JsonUtil.ObjectMapperFactory.getObjectMapper());
    }

    @Bean("batchJobRedisTemplate")
    public RedisTemplate<String, Object> batchJobRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return buildTemplate(redisConnectionFactory, JsonUtil.ObjectMapperFactory.getObjectMapper());
    }

    private RedisTemplate<String, Object> buildTemplate(RedisConnectionFactory redisConnectionFactory,
                                                        ObjectMapper objectMapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                objectMapper, Object.class);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }
}
