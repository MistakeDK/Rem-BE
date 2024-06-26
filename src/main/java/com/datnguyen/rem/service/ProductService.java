package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.ProductRequest;
import com.datnguyen.rem.dto.request.ProductUpdateRequest;
import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.dto.response.ProductDetailResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface ProductService {
    void addProduct(ProductRequest request) throws IOException;
    ProductDetailResponse getProductById(String id) throws JsonProcessingException;
    PageResponse<?> getList(int pageNo, int pageSize, String sorts, String category, String... search);
    void edit(String id, ProductUpdateRequest request);
}
