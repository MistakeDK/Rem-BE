package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.ProductRequest;
import com.datnguyen.rem.entity.Product;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.ProductMapper;
import com.datnguyen.rem.repository.CategoryRepository;
import com.datnguyen.rem.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper mapper;
    public void addProduct(ProductRequest request) throws IOException {
        Product product=mapper.toProduct(request);
        product.setCategory(categoryRepository.
                findById(request.getCategoryId()).
                orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
        productRepository.save(product);
    }
    public List<Product> getList(){
        List<Product> products= productRepository.findAll();
        return products;
    }
}
