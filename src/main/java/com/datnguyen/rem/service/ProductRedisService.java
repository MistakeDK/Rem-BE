package com.datnguyen.rem.service;

import com.datnguyen.rem.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProductRedisService {
    void saveProduct(Product product) throws JsonProcessingException;

    Product getProduct(String id) throws JsonProcessingException;

    void deleteProductCache(String id);
}
