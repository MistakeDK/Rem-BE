package com.datnguyen.rem.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Value("${redis.host}")
    String host;
    @Value("${redis.port}")
    Integer port;
    @Bean
    public JedisConnectionFactory connectionFactory(){
        RedisStandaloneConfiguration configuration=new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        return new JedisConnectionFactory(configuration);
    }
    @Bean
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> template=new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
    @Bean
    public HashOperations<String,String,Object> hashOperations(){
        return redisTemplate().opsForHash();
    }
    @Bean
    public SetOperations<String,Object> SetOperations(){
        return redisTemplate().opsForSet();
    }
    @Bean
    public ObjectMapper redisObjectMapper(){
        ObjectMapper objectMapper=new ObjectMapper();
        SimpleModule module=new SimpleModule();
        module.addSerializer(Date.class,new DateSerializer());
        module.addDeserializer(Date.class,new DateDeserializers.DateDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

}
