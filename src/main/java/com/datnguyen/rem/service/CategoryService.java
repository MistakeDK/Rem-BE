package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.CategoryRequest;
import com.datnguyen.rem.entity.Category;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    public void AddCategory(CategoryRequest request){
        if(categoryRepository.existsByName(request.getName())){
            throw new  AppException(ErrorCode.CATEGORY_EXISTED);
        }
        Category category=Category.builder().name(request.getName()).build();
        categoryRepository.save(category);
    }
    public List<Category> getList(){
        var result=categoryRepository.findAll();
        return result.stream().toList();
    }
    public void deleteByID(String id){
        categoryRepository.deleteById(id);
    }
}
