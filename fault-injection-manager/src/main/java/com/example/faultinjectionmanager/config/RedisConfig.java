package com.example.faultinjectionmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
@Configuration
public class RedisConfig {
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory){
        //创建并返回一个StringRedisTemplate，用于操作redis中的字符串数据
        //使用提供的RedisConnectionFactory建立与redis的服务器连接
        return new StringRedisTemplate(connectionFactory);
    }
}
