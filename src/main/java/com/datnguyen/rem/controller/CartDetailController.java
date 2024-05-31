package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.CartDetailRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.service.CartDetailService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CartDetailController {
    CartDetailService cartDetailService;
    @GetMapping("/getList/{idUser}")
    ResponseEntity<ApiResponse<?>> getList(@PathVariable(name = "idUser") String id ){
        var cartResponse=cartDetailService.getList(id);
        ApiResponse<?> apiResponse=ApiResponse.builder().result(cartResponse).build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    @PostMapping("/changeQuantity/{idUser}")
    ResponseEntity<ApiResponse<?>>  ChangeQuantity(@RequestBody CartDetailRequest request,
                                                   @PathVariable String idUser){
        cartDetailService.changeQuantity(request,idUser);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("Change Quantity Success").build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
