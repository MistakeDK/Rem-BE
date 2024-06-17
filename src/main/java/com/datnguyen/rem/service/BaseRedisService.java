package com.datnguyen.rem.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseRedisService {
    void Set(String key,String value);
    void Set(String key,String value,long ttl);
    boolean keyExists(String key);
    void SetTimeToLive(String key,long timeoutInSecond);
    void hashSet(String key,String field,Object value);
    boolean hashExists(String key,String field);
    Object get(String key);
    Map<String,Object> getField(String key);
    Object hashGet(String key,String field);
    List<Object> hashGetByFieldPrefix(String key,String filedPrefix);
    Set<String> getFieldPreFixes(String key);
    void delete(String key);
    void delete(String key, String field);
    void delete(String key,List<String> fields);
}
