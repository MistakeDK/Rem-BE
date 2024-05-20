package com.datnguyen.rem.controller;

import com.cloudinary.Api;
import com.datnguyen.rem.dto.request.ProductRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.service.CloundinaryService;
import com.datnguyen.rem.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ProductController {
    ProductService productService;
    CloundinaryService cloundinaryService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest request) throws IOException {
        productService.addProduct(request);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("Add product success").build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping()
    public ResponseEntity<?> getList(){
        var result=productService.getList();
        ApiResponse<?> apiResponse=ApiResponse.builder().result(result).build();
        return  ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<Map> uploadImg(@RequestParam("image")MultipartFile file) throws IOException {
        Map data= cloundinaryService.uploadFile(file);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }
}
