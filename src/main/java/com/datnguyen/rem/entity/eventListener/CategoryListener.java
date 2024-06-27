package com.datnguyen.rem.entity.eventListener;

import com.datnguyen.rem.entity.Category;
import com.datnguyen.rem.service.impl.CategoryRedisServiceImpl;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryListener {
    private final CategoryRedisServiceImpl categoryRedisService;
    @PreUpdate
    @PrePersist
    public void PrePersist(Category category){
        categoryRedisService.deleteCategoryCache();
    }
}
