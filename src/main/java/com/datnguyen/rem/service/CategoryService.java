package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.CategoryRequest;
import com.datnguyen.rem.dto.response.CategoryResponse;
import com.datnguyen.rem.dto.response.PageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    void AddCategory(CategoryRequest request);

    PageResponse<?> getList(Pageable pageable, String name);

}
