package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.PermissionRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.dto.response.PermissionResponse;
import com.datnguyen.rem.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/permissions")
public class PermissionController {
    PermissionService permissionService;
    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest permissionRequest){
        return ApiResponse.<PermissionResponse>builder().
                result(permissionService.create(permissionRequest)).build();
    }
    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll(){
        return ApiResponse.<List<PermissionResponse>>builder().result(permissionService.getAll()).build();
    }
    @DeleteMapping("/{permission}")
    ApiResponse<String> delete(@PathVariable String permission){
        permissionService.delete(permission);
        return ApiResponse.<String>builder().message("delete success").build();
    }
}
