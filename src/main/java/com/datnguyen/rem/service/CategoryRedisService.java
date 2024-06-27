package com.datnguyen.rem.service;

import com.datnguyen.rem.entity.Category;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CategoryRedisService {
    void saveAllCategory(List<Category> categoryList) throws JsonProcessingException;

    List<Category> getListCategory() throws JsonProcessingException;

    void deleteCategoryCache();
}
