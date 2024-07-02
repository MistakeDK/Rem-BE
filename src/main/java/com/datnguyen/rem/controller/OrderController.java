package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.OrderRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.service.impl.OrderServiceImpl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderController {
    OrderServiceImpl service;
    @PostMapping("")
    ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest request){
        var result= service.createOrder(request);
        ApiResponse<?> apiResponse=ApiResponse.builder().result(result).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping("/{idUser}")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<ApiResponse<?>> getOrderByIdUser(@PathVariable("idUser") String id,
                                                    @RequestParam(defaultValue = "0",required = false) int pageNo,
                                                    @RequestParam(defaultValue = "4",required = false) int pageSize){
        var listOrder=service.getAllOrderByIdUser(id,pageNo,pageSize);
        ApiResponse<?> apiResponse=ApiResponse.builder()
                .result(listOrder)
                .build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping("/getList")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<ApiResponse<?>> getList(Pageable pageable,
                                           @RequestParam(required = false) String... order){
        var listOrder=service.getList(pageable,order);
        ApiResponse<?> apiResponse=ApiResponse.builder().result(listOrder).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<?> getById(@PathVariable String id){
        var order=service.getById(id);
        ApiResponse<?> apiResponse=ApiResponse.builder().result(order).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @PatchMapping("/changeStatus/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<?> ChangeStatus(@PathVariable String id){
        service.changeStatus(id);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("Change Status Order Success").build();
        return ResponseEntity.ok().body(apiResponse);
    }
}
