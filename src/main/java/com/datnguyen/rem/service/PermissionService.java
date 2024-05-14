package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.PermissionRequest;
import com.datnguyen.rem.dto.response.PermissionResponse;
import com.datnguyen.rem.entity.Permission;
import com.datnguyen.rem.mapper.PermissionMapper;
import com.datnguyen.rem.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    public PermissionResponse create(PermissionRequest request){
        Permission permission=permissionMapper.toPermission(request);
        permissionRepository.save(permission);
        return permissionMapper.toPermissonResponse(permission);
    }
    public List<PermissionResponse> getAll(){
        var permissions=permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissonResponse).toList();
    }
    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }
}
