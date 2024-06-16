package com.datnguyen.rem.repository.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class InvalidTokenRepository{
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void saveInvalidToken(String jit,Long ttl){
        redisTemplate.opsForValue().set(jit,"invalid",ttl, TimeUnit.SECONDS);
        redisTemplate.opsForList();
    }
    public boolean isTokenInvalid(String jit){
        return Boolean.TRUE.equals(redisTemplate.hasKey(jit));
    }
}
