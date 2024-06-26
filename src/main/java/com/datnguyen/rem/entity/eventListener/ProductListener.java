package com.datnguyen.rem.entity.eventListener;

import com.datnguyen.rem.entity.Product;
import com.datnguyen.rem.service.impl.ProductRedisServiceImpl;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductListener {
    private final ProductRedisServiceImpl productRedisServiceImpl;
    @PostUpdate
    public void PostUpdate(Product product){
        productRedisServiceImpl.deleteProductCache(product.getId());
    }
}
