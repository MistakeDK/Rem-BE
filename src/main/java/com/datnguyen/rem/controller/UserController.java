package com.datnguyen.rem.controller;

import com.cloudinary.Api;
import com.datnguyen.rem.dto.request.ChangePasswordRequest;
import com.datnguyen.rem.dto.request.UserCreationRequest;
import com.datnguyen.rem.dto.request.UserUpdateRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.dto.response.UserResponse;
import com.datnguyen.rem.service.impl.UserServiceImpl;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Slf4j
public class UserController {
    UserServiceImpl userServiceImpl;
    @PostMapping
    ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody UserCreationRequest request)
            throws MessagingException, IOException, TemplateException {
        ApiResponse<UserResponse> apiResponse= ApiResponse.<UserResponse>builder()
                .result(userServiceImpl.createUser(request)).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping
    ResponseEntity<ApiResponse<?>> getList(Pageable pageable,
                                           @RequestParam(required = false) String... user){
        var authentication=SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        var listUser=userServiceImpl.getList(pageable,user);
        ApiResponse<?> apiResponse=ApiResponse.builder().result(listUser).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{idUser}")
    ResponseEntity<ApiResponse<?>> getUser(@PathVariable("idUser") String id){
        var authentication=SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        ApiResponse<?> apiResponse=ApiResponse.builder().result(userServiceImpl.getUser(id)).build();
        return  ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping("/myInfo")
    ResponseEntity<ApiResponse<?>> getMyInfo(){
        ApiResponse<UserResponse> apiResponse=ApiResponse.<UserResponse>builder().result(userServiceImpl.getMyInfo()).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @PutMapping("/{idUser}")
    ResponseEntity<ApiResponse<?>> UpdateUser(@PathVariable("idUser") String id,@Valid @RequestBody UserUpdateRequest request){
        ApiResponse<?> apiResponse=ApiResponse.builder().result(userServiceImpl.updateUser(id,request)).build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @PatchMapping("/{username}")
    ResponseEntity<?> processVerifyUser(@RequestParam String code,
                                        @PathVariable String username){
        userServiceImpl.processVerify(username,code);
        ApiResponse<?> apiResponse= ApiResponse.builder().message("Active Success").build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @PatchMapping("/changePassword/{idUser}")
    ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                     @PathVariable("idUser") String id){
        userServiceImpl.ChangePassword(request,id);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("Password has been change success").build();
        return ResponseEntity.ok().body(apiResponse);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/changeStatus/{email}")
    ResponseEntity<?> changeStatus(@PathVariable String email){
        userServiceImpl.changeStatus(email);
        ApiResponse<?> apiResponse=ApiResponse.builder().message("Change Status User success").build();
        return ResponseEntity.ok().body(apiResponse);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stat")
    ResponseEntity<?> getStat(){
        var result= userServiceImpl.getStat();
        ApiResponse<?> apiResponse=ApiResponse.builder().result(result).build();
        return ResponseEntity.ok(apiResponse);
    }

}
