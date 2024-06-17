package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.service.BaseRedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BaseRedisServiceImpl implements BaseRedisService {
    private final RedisTemplate<String,Object> redisTemplate;
    private final HashOperations<String,String,Object> hashOperations;
    @Override
    public void Set(String key, String value) {
        redisTemplate.opsForValue().set(key,value);
    }
    @Override
    public void Set(String key,String value,long ttl){
        redisTemplate.opsForValue().set(key,value,ttl,TimeUnit.SECONDS);
    }

    @Override
    public void SetTimeToLive(String key, long timeoutInSecond) {
        redisTemplate.expire(key,timeoutInSecond, TimeUnit.SECONDS);
    }

    @Override
    public boolean keyExists(String key){
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void hashSet(String key, String field, Object value) {
        hashOperations.put(key,field,value);
    }

    @Override
    public boolean hashExists(String key, String field) {
        return hashOperations.hasKey(key,field);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<String, Object> getField(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public Object hashGet(String key, String field) {
        return hashOperations.get(key,field);
    }

    @Override
    public List<Object> hashGetByFieldPrefix(String key, String filedPrefix) {
        List<Object> objects=new ArrayList<>();
        Map<String,Object> hashEntries=hashOperations.entries(key);
        for(Map.Entry<String,Object> entry:hashEntries.entrySet()){
            objects.add(entry.getValue());
        }
        return objects;
    }

    @Override
    public Set<String> getFieldPreFixes(String key) {
        return hashOperations.entries(key).keySet();
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(String key, String field) {
        hashOperations.delete(key,field);
    }

    @Override
    public void delete(String key, List<String> fields) {
        for(String field:fields){
            hashOperations.delete(key,field);
        }
    }
}
