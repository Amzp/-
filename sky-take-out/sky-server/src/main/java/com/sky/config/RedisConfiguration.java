package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * ClassName: RedisConfiguration
 * Package: com.sky.config
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/6 上午9:59
 * @Version 1.0
 */
@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean // @Bean注解标记在方法上，表明该方法会返回一个对象，该对象要注册为Spring应用程序上下文中的Bean。在这种情况下，方法返回的RedisTemplate实例将被Spring管理。
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory
    // 方法参数RedisConnectionFactory redisConnectionFactory是一个RedisConnectionFactory实例，Spring会自动注入这个依赖。RedisConnectionFactory是一个用于创建与Redis服务器连接的工厂接口。
    ){
        log.info("开始创建RedisTemplate对象...");
        // 1. 创建RedisTemplate对象
        RedisTemplate redisTemplate = new RedisTemplate();

        // 2. 设置Redis的连接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 3. 设置Redis key的序列化器。在这里，使用了StringRedisSerializer作为键的序列化器，这意味着在Redis操作中，键会被当作字符串处理。
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 返回配置好的RedisTemplate实例。
        return redisTemplate;
    }
}
