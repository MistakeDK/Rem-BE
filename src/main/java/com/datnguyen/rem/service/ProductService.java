package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.ProductRequest;
import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.dto.response.ProductDetailResponse;
import com.datnguyen.rem.dto.response.ProductResponse;
import com.datnguyen.rem.entity.Product;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.ProductMapper;
import com.datnguyen.rem.repository.CategoryRepository;
import com.datnguyen.rem.repository.ProductRepository;
import com.datnguyen.rem.repository.ReviewRepository;
import com.datnguyen.rem.repository.SearchRepository;
import com.datnguyen.rem.utils.SortUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ProductService {
    ProductRepository productRepository;
    SearchRepository searchRepository;
    CategoryRepository categoryRepository;
    ReviewRepository reviewRepository;
    ProductMapper mapper;
    public void addProduct(ProductRequest request) throws IOException {
        Product product=mapper.toProduct(request);
        product.setCategory(categoryRepository.
                findById(request.getCategoryId()).
                orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
        productRepository.save(product);
    }
    public ProductDetailResponse getProductById(String id){
        var product= productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
        return mapper.toProductDetailResponse(product,reviewRepository);
    }
    public PageResponse<?> getList(int pageNo, int pageSize, String sorts,String category,String... search){
        return searchRepository.advancedSearchProductWithCriteria(pageNo,pageSize,sorts,category,search);
    }
}
