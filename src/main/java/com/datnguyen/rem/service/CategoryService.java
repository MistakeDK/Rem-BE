package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.CategoryRequest;
import com.datnguyen.rem.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    void AddCategory(CategoryRequest request);
    List<CategoryResponse> getList();
}
