package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.dto.request.CategoryRequest;
import com.datnguyen.rem.dto.response.CategoryResponse;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
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
    public List<CategoryResponse> getList(){
        var result=categoryRepository.findAll();
        return result.stream().map(categoryMapper::toCategoryResponse).toList();
    }
    public void deleteByID(String id){
        categoryRepository.deleteById(id);
    }
}
