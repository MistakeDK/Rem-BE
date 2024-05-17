package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.UserCreationRequest;
import com.datnguyen.rem.dto.request.UserUpdateRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.dto.response.UserResponse;
import com.datnguyen.rem.entity.User;
import com.datnguyen.rem.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Slf4j
public class UserController {
    UserService userService;
    @PostMapping
    ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody UserCreationRequest request){
        ApiResponse<UserResponse> apiResponse= ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request)).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping
    ResponseEntity<ApiResponse<?>> getList(){
        var authentication=SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        ApiResponse<?> apiResponse=ApiResponse.builder().result(userService.getList()).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping("/{idUser}")
    ResponseEntity<ApiResponse<?>> getUser(@PathVariable("idUser") String id){
        var authentication=SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        ApiResponse<?> apiResponse=ApiResponse.builder().result(userService.getUser(id)).build();
        return  ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping("/myInfo")
    ResponseEntity<ApiResponse<?>> getMyInfo(){
        ApiResponse apiResponse=ApiResponse.<UserResponse>builder().result(userService.getMyInfo()).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @PutMapping("/{idUser}")
    ResponseEntity<ApiResponse<?>> UpdateUser(@PathVariable("idUser") String id,@Valid @RequestBody UserUpdateRequest request){
        ApiResponse<?> apiResponse=ApiResponse.builder().result(userService.updateUser(id,request)).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @DeleteMapping("/{idUser}")
    ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable("idUser") String id){
        userService.deleteUser(id);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("delete success").build();
        return ResponseEntity.ok().body(apiResponse);
    }
}