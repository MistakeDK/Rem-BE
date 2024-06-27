package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.entity.Category;
import com.datnguyen.rem.service.CategoryRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryRedisServiceImpl implements CategoryRedisService {
    private final BaseRedisServiceImpl baseRedisService;
    private final ObjectMapper redisObjectMapper;
    @Override
    public void saveAllCategory(List<Category> categoryList) throws JsonProcessingException {
        String categoryListJson= redisObjectMapper.writeValueAsString(categoryList);
        baseRedisService.Set("category",categoryListJson);
    }
    @Override
    public List<Category> getListCategory() throws JsonProcessingException {
        String categoryListJson= (String) baseRedisService.get("category");
        if(categoryListJson==null){
            return null;
        }
        return redisObjectMapper.readValue(categoryListJson, new TypeReference<List<Category>>() {});
    }
    @Override
    public void deleteCategoryCache(){
        baseRedisService.delete("category");
    }
}
