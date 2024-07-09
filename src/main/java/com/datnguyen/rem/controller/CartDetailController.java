package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.CartDetailRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.service.impl.CartDetailServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CartDetailController {
    CartDetailServiceImpl cartDetailServiceImpl;
    @GetMapping("/getList/{idUser}")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<ApiResponse<?>> getList(@PathVariable(name = "idUser") String id ){
        var cartResponse= cartDetailServiceImpl.getList(id);
        ApiResponse<?> apiResponse=ApiResponse.builder().result(cartResponse).build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PostMapping("/changeQuantity/{idUser}")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<ApiResponse<?>>  ChangeQuantity(@Valid @RequestBody CartDetailRequest request,
                                                   @PathVariable String idUser)
            throws JsonProcessingException {
        cartDetailServiceImpl.changeQuantity(request,idUser);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("Change Quantity Success").build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
