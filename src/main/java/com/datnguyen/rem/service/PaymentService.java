package com.datnguyen.rem.service;

import com.datnguyen.rem.configuration.payment.VNPayConfig;
import com.datnguyen.rem.dto.request.OrderRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPayConfig vnPayConfig;
    private final OrderService orderService;
    public ApiResponse<?> createVNPayPayment(HttpServletRequest request, OrderRequest orderRequest){
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtils.getIpAddress(request));
        String queryUrl = VNPayUtils.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtils.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtils.hmacSHA512(vnPayConfig.getSecretkey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnpPayUrl() + "?" + queryUrl;
        orderService.createOrder(orderRequest,vnpParamsMap.get("vnp_TxnRef"));
        return ApiResponse.builder()
                .result(paymentUrl)
                .build();
    }
}
