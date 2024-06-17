package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.ReviewRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.service.impl.ReviewServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ReviewController {
    ReviewServiceImpl service;
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping()
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest request){
        service.createReview(request);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("Create review Success").build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping("/{idProduct}")
    public ResponseEntity<?> getAllReview(@RequestParam(required = false,defaultValue = "0") int pageNo,
                                       @RequestParam(required = false,defaultValue = "10") int pageSize,
                                       @PathVariable String idProduct){
        var listReviewResponse=service.getList(idProduct,pageNo,pageSize);
        ApiResponse<?> apiResponse=ApiResponse.builder().result(listReviewResponse).build();
        return ResponseEntity.ok().body(apiResponse);
    }
}
