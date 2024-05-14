package com.datnguyen.rem.mapper;

import com.datnguyen.rem.dto.request.PermissionRequest;
import com.datnguyen.rem.dto.request.RoleRequest;
import com.datnguyen.rem.dto.response.PermissionResponse;
import com.datnguyen.rem.dto.response.RoleResponse;
import com.datnguyen.rem.entity.Permission;
import com.datnguyen.rem.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permission",ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
