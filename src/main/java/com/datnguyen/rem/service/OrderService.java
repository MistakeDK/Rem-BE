package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.OrderRequest;
import com.datnguyen.rem.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    String createOrder(OrderRequest request);
    void paidOrderById(String id);
    PageResponse<?> getAllOrderByIdUser(String id,int pageNo,int pageSize);

    PageResponse<?> getList(Pageable pageable, String[] order);
}
