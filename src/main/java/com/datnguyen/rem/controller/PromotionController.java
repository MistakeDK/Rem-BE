package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.PromotionRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.service.PromotionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promotions")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PromotionController {
    PromotionService service;
    @PostMapping()
    ResponseEntity<?> addPromotion(@Valid @RequestBody PromotionRequest promotionRequest){
        service.addPromotion(promotionRequest);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("Add Success").build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping("/{id}")
    ResponseEntity<?> getPromotionById(@PathVariable String id){
        var promotion=service.getPromotionByID(id);
        ApiResponse<?> apiResponse=ApiResponse.builder().result(promotion).build();
        return ResponseEntity.ok().body(apiResponse);
    }
}
