package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.RoleRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.dto.response.RoleResponse;
import com.datnguyen.rem.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/roles")
public class RoleController {
    RoleService roleService;
    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<RoleResponse>> getList(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

}
