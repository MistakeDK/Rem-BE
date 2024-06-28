package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.PromotionRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.service.impl.PromotionServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promotions")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PromotionController {
    PromotionServiceImpl service;
    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<?> addPromotion(@Valid @RequestBody PromotionRequest promotionRequest){
        service.addPromotion(promotionRequest);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("Add Success").build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping()
    ResponseEntity<?> getList(Pageable pageable,
                              @RequestParam(required = false) String... promotion){
        var result=service.getList(pageable,promotion);
        ApiResponse<?> apiResponse=ApiResponse.builder().result(result).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping("/{id}")
    ResponseEntity<?> getPromotionById(@PathVariable String id) throws JsonProcessingException {
        var promotion=service.getPromotionByID(id);
        ApiResponse<?> apiResponse=ApiResponse.builder().result(promotion).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @PatchMapping("/ChangeStatus/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<?> changeStatus(@PathVariable String id){
        service.ChangePromotionStatus(id);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("Change Status promotion Success").build();
        return ResponseEntity.ok().body(apiResponse);
    }
}
