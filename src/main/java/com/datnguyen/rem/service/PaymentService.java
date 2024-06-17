package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    ApiResponse<?> createVNPayPayment(HttpServletRequest request, String orderId);
}
