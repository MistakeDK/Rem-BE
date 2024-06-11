package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.OrderRequest;
import com.datnguyen.rem.service.OrderService;
import com.datnguyen.rem.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    PaymentService paymentService;
    OrderService orderService;
    @Value("${VNPay.successUrl}")
    @NonFinal
    String successUrl;
    @NonFinal
    @Value("${VNPay.failUrl}")
    String failUrl;
    @GetMapping("/vn-pay/{orderId}")
    ResponseEntity<?> CreateUrlPaymentVNPay(HttpServletRequest request,
                                            @PathVariable String orderId){
        var result=paymentService.createVNPayPayment(request,orderId);
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
            orderService.paidOrderById(orderId);
        }else {
            urlRedirect=failUrl;
        }
        response.sendRedirect(urlRedirect);
    }
}
