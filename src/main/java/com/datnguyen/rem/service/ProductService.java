package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.ProductRequest;
import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.dto.response.ProductResponse;
import com.datnguyen.rem.entity.Product;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.ProductMapper;
import com.datnguyen.rem.repository.CategoryRepository;
import com.datnguyen.rem.repository.ProductRepository;
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
    ProductMapper mapper;
    public void addProduct(ProductRequest request) throws IOException {
        Product product=mapper.toProduct(request);
        product.setCategory(categoryRepository.
                findById(request.getCategoryId()).
                orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED)));
        productRepository.save(product);
    }
    public ProductResponse getProductById(String id){
        var product= productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
        return mapper.toProductResponse(product);
    }
    public PageResponse<?> getList(int pageNo, int pageSize, String sorts,String category,String... search){
//        if(pageNo>0){
//           pageNo=pageNo-1;
//        }
//        List<Sort.Order> orders=new ArrayList<>();
//        for (String sortBy:sorts){
//            orders.add(SortUtils.createSortOrder(sortBy));
//            //First element in order is highest prioritize
//        }
//        Pageable pageable= PageRequest.of(pageNo,pageSize,Sort.by(orders));
//        var products= productRepository.findAll(pageable);
//        return PageResponse.builder()
//                .pageNo(pageNo==0?pageNo+1:pageNo)
//                .totalItem(products.getTotalElements())
//                .pageSize(pageSize)
//                .totalPage(products.getTotalPages())
//                .items(products.stream().map(mapper::toProductResponse).toList())
//                .build();
//        return searchRepository.getListProduct(pageNo,pageSize,search,sorts);
        return searchRepository.advancedSearchProductWithCriteria(pageNo,pageSize,sorts,category,search);
    }
}
