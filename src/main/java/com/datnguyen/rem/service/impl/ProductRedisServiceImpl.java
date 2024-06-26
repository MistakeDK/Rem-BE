package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.entity.Product;
import com.datnguyen.rem.service.ProductRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductRedisServiceImpl implements ProductRedisService {
    private final BaseRedisServiceImpl baseRedisService;
    private final ObjectMapper redisObjectMapper;
    @Override
    public void saveProduct(Product product) throws JsonProcessingException {
        String productJson=redisObjectMapper.writeValueAsString(product);
        baseRedisService.Set(String.format("product:%s",product.getId()),productJson);
    }
    @Override
    public Product getProduct(String id) throws JsonProcessingException {
        String productJson=(String) baseRedisService.get(String.format("product:%s",id));
        if(productJson==null){
            return null;
        }
        return redisObjectMapper.readValue(productJson,Product.class);
    }
    @Override
    public void deleteProductCache(String id){
        baseRedisService.delete(String.format("product:%s",id));
    }
}
