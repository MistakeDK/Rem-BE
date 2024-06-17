package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.dto.request.ProductRequest;
import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.dto.response.ProductDetailResponse;
import com.datnguyen.rem.entity.Product;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.ProductMapper;
import com.datnguyen.rem.repository.CategoryRepository;
import com.datnguyen.rem.repository.ProductRepository;
import com.datnguyen.rem.repository.ReviewRepository;
import com.datnguyen.rem.repository.SearchRepository;
import com.datnguyen.rem.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    SearchRepository searchRepository;
    CategoryRepository categoryRepository;
    ReviewRepository reviewRepository;
    ProductMapper mapper;
    @Transactional
    @Override
    public void addProduct(ProductRequest request) throws IOException {
        Product product=mapper.toProduct(request);
        product.setCategory(categoryRepository.
                findById(request.getCategoryId()).
                orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
        productRepository.save(product);
    }
    @Override
    public ProductDetailResponse getProductById(String id){
        var product= productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
        return mapper.toProductDetailResponse(product,reviewRepository);
    }
    @Override
    public PageResponse<?> getList(int pageNo, int pageSize, String sorts,String category,String... search){
        return searchRepository.advancedSearchProductWithCriteria(pageNo,pageSize,sorts,category,search);
    }
}
