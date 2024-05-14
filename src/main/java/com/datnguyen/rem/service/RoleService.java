package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.RoleRequest;
import com.datnguyen.rem.dto.response.RoleResponse;
import com.datnguyen.rem.mapper.RoleMapper;
import com.datnguyen.rem.repository.PermissionRepository;
import com.datnguyen.rem.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;
    public RoleResponse create(RoleRequest request){
        var role=roleMapper.toRole(request);
        var permission= permissionRepository.findAllById(request.getPermission());
        role.setPermission(new HashSet<>(permission));
        roleRepository.save(role);
        RoleResponse response=roleMapper.toRoleResponse(role);
        return response;
    }
    public List<RoleResponse> getAll(){
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }
    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
