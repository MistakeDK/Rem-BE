package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.CategoryRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.service.impl.CategoryServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryController {
    CategoryServiceImpl categoryServiceImpl;
    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> AddCategory(@RequestBody CategoryRequest request){
        categoryServiceImpl.AddCategory(request);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("Add new Category Success").build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeleteCategory(@PathVariable String id){
        categoryServiceImpl.deleteByID(id);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("Delete Success").build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @GetMapping("")
    public  ResponseEntity<?> getListCategory(){
        ApiResponse<?> result= ApiResponse.builder().result(categoryServiceImpl.getList()).build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
