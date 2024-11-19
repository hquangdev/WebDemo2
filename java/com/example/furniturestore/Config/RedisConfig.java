package com.example.furniturestore.Config;

import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedissonClient redissonClient() {
        // Cấu hình kết nối Redisson đến Redis
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");  // Địa chỉ Redis của bạn

        // Tạo RedissonClient
        return org.redisson.Redisson.create(config);
    }
}
