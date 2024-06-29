package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.dto.request.CategoryRequest;
import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.entity.Category;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.CategoryMapper;
import com.datnguyen.rem.repository.CategoryRepository;
import com.datnguyen.rem.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    CategoryRedisServiceImpl categoryRedisService;
    @Override
    @Transactional
    public void AddCategory(CategoryRequest request){
        if(categoryRepository.existsByName(request.getName())){
            throw new  AppException(ErrorCode.CATEGORY_EXISTED);
        }
        Category category=categoryMapper.toCategory(request);
        categoryRepository.save(category);
    }

    @Override
    public PageResponse<?> getList(Pageable pageable, String name) {
        Page<Category> categoryPage=null;
        if(name!=null){
            categoryPage= categoryRepository.findByNameContains(name,pageable);
        }
        else {
            categoryPage=categoryRepository.findAll(pageable);
        }
        return PageResponse.builder()
                .pageNo(categoryPage.getNumber())
                .pageSize(categoryPage.getSize())
                .totalPage(categoryPage.getTotalPages())
                .items(categoryPage.getContent().stream().map(categoryMapper::toCategoryResponse).toList())
                .build();
    }
}
