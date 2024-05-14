package com.datnguyen.rem.mapper;

import com.datnguyen.rem.dto.request.PermissionRequest;
import com.datnguyen.rem.dto.request.UserCreationRequest;
import com.datnguyen.rem.dto.request.UserUpdateRequest;
import com.datnguyen.rem.dto.response.PermissionResponse;
import com.datnguyen.rem.dto.response.UserResponse;
import com.datnguyen.rem.entity.Permission;
import com.datnguyen.rem.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissonResponse(Permission permission);
}
