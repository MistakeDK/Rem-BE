package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.dto.request.UserCreationRequest;
import com.datnguyen.rem.dto.request.UserUpdateRequest;
import com.datnguyen.rem.dto.response.UserResponse;
import com.datnguyen.rem.entity.User;
import com.datnguyen.rem.enums.Role;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.UserMapper;
import com.datnguyen.rem.repository.UserRepository;
import com.datnguyen.rem.service.UserService;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    MailServiceImpl mailServiceImpl;
    BaseRedisServiceImpl baseRedisService;
    @Transactional
    public UserResponse createUser(UserCreationRequest request)
            throws MessagingException, IOException, TemplateException {
        if(userRepository.existsByusername(request.getUsername())){
            throw  new AppException(ErrorCode.USER_EXISTED);
        }
        User user=userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        mailServiceImpl.sendVerificationEmail(user);
        baseRedisService.hashSet(user.getUsername(),"verificationCode",user.getVerificationCode());
        baseRedisService.SetTimeToLive(user.getUsername(),300);// expire in 5 minutes
        return userMapper.toUserResponse(user);
    }
    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponse> getList(){
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST)));
    }
    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getMyInfo(){
        var context= SecurityContextHolder.getContext();
        String name=context.getAuthentication().getName();
        return userMapper.toUserResponse(userRepository.findByusername(name).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST)));
    }
    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse updateUser(String id, UserUpdateRequest request){
        User user=userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST));
        userMapper.updateUser(user,request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String id){
        userRepository.deleteById(id);
    }
    @Override
    @Transactional
    public void processVerify(String userName,String code){
        User user=userRepository.findByusername(userName)
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST));
        String verificationCode= baseRedisService.hashGet(userName,"verificationCode").toString();
        if(!Objects.equals(verificationCode, code)){
            throw new AppException(ErrorCode.VERIFY_CODE_NOT_VALID);
        }
        user.setIsActive(true);
        baseRedisService.delete(userName);
        userRepository.save(user);
    }

}
