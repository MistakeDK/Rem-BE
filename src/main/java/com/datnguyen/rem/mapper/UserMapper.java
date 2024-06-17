package com.datnguyen.rem.mapper;

import com.datnguyen.rem.dto.request.UserCreationRequest;
import com.datnguyen.rem.dto.request.UserUpdateRequest;
import com.datnguyen.rem.dto.response.UserResponse;
import com.datnguyen.rem.entity.User;
import com.datnguyen.rem.enums.Role;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    @AfterMapping
    default void SetProperty(@MappingTarget User target,UserCreationRequest request){
        String randomCode= UUID.randomUUID().toString();
        target.setIsBan(false);
        target.setIsActive(false);
        target.setRole(Role.USER);
        target.setVerificationCode(randomCode);
    }




    void updateUser(@MappingTarget User user, UserUpdateRequest request);
    UserResponse toUserResponse(User user);
}
