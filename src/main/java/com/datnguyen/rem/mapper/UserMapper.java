package com.datnguyen.rem.mapper;

import com.datnguyen.rem.dto.request.UserCreationRequest;
import com.datnguyen.rem.dto.request.UserUpdateRequest;
import com.datnguyen.rem.dto.response.OutboundUserResponse;
import com.datnguyen.rem.dto.response.UserResponse;
import com.datnguyen.rem.entity.User;
import com.datnguyen.rem.enums.Role;
import com.datnguyen.rem.enums.UserProvide;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
    @Mapping(ignore = true,target = "id")
    User toUserFromOutBound(OutboundUserResponse response);
    @AfterMapping
    default void SetProperty(@MappingTarget User target,OutboundUserResponse response){
        target.setUsername(response.getGivenName());
        target.setIsBan(false);
        target.setIsActive(true);
        target.setRole(Role.USER);
        target.setUserProvide(UserProvide.GOOGLE);
    }



    void updateUser(@MappingTarget User user, UserUpdateRequest request);
    UserResponse toUserResponse(User user);
}
