package com.datnguyen.rem.controller;

import com.datnguyen.rem.service.impl.OrderServiceImpl;
import com.datnguyen.rem.service.impl.PaymentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/payment")
public class PaymentController {
    PaymentServiceImpl paymentServiceImpl;
    OrderServiceImpl orderServiceImpl;
    @Value("${VNPay.successUrl}")
    @NonFinal
    String successUrl;
    @NonFinal
    @Value("${VNPay.failUrl}")
    String failUrl;
    @GetMapping("/vn-pay/{orderId}")
    ResponseEntity<?> CreateUrlPaymentVNPay(HttpServletRequest request,
                                            @PathVariable String orderId){
        var result= paymentServiceImpl.createVNPayPayment(request,orderId);
        return ResponseEntity.ok().body(result);
    }
    @GetMapping("/vn-pay-callback")
    void payCallbackHandler(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestParam String vnp_ResponseCode
                              ) throws IOException {
        String urlRedirect=successUrl;
        String status=request.getParameter("vnp_ResponseCode");
        String orderId=request.getParameter("vnp_TxnRef");
        if(status.equals("00")){
            orderServiceImpl.paidOrderById(orderId);
        }else {
            urlRedirect=failUrl;
        }
        response.sendRedirect(urlRedirect);
    }
}
