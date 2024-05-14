package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.UserCreationRequest;
import com.datnguyen.rem.dto.request.UserUpdateRequest;
import com.datnguyen.rem.dto.response.UserResponse;
import com.datnguyen.rem.entity.Role;
import com.datnguyen.rem.entity.User;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.UserMapper;
import com.datnguyen.rem.repository.RoleRepository;
import com.datnguyen.rem.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    public UserResponse createUser(UserCreationRequest request){
        if(userRepository.existsByusername(request.getUsername())){
            throw  new AppException(ErrorCode.USER_EXISTED);
        }
        User user=userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roleUser=new  HashSet<Role>();
        roleUser.add(roleRepository.findById("USER").orElseThrow(()->new AppException(ErrorCode.CATCH_EXCEPTION_FAIL)));
        user.setRoles(new HashSet<Role>(roleUser));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PreAuthorize("hasAuthority('CREATE')")
    public List<UserResponse> getList(){
        return  userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST)));
    }
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getMyInfo(){
        var context= SecurityContextHolder.getContext();
        String name=context.getAuthentication().getName();
        return userMapper.toUserResponse(userRepository.findByusername(name).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST)));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request){
        User user=userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST));
        userMapper.updateUser(user,request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles=roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return  userMapper.toUserResponse(userRepository.save(user));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String id){
       userRepository.deleteById(id);
    }
}
