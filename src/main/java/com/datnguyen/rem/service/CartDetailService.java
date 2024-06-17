package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.CartDetailRequest;
import com.datnguyen.rem.dto.response.CartDetailResponse;

import java.util.List;

public interface CartDetailService {
    List<CartDetailResponse> getList(String idUser);
    void changeQuantity(CartDetailRequest request, String idUser);
}
