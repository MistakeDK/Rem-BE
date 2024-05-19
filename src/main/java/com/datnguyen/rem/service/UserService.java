package com.datnguyen.rem.service;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.datnguyen.rem.dto.request.UserCreationRequest;
import com.datnguyen.rem.dto.request.UserUpdateRequest;
import com.datnguyen.rem.dto.response.UserResponse;
import com.datnguyen.rem.entity.User;
import com.datnguyen.rem.enums.Role;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.UserMapper;
import com.datnguyen.rem.repository.UserRepository;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.id.uuid.StandardRandomStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    MailService mailService;
    public UserResponse createUser(UserCreationRequest request)
            throws MessagingException, IOException, TemplateException {
        if(userRepository.existsByusername(request.getUsername())){
            throw  new AppException(ErrorCode.USER_EXISTED);
        }
        User user=userMapper.toUser(request);
        String randomCode= UUID.randomUUID().toString();
        user.setVerificationCode(randomCode);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER.name());
        user.setIsActive(false);
        userRepository.save(user);
        mailService.sendVerificationEmail(user);
        return userMapper.toUserResponse(user);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserResponse updateUser(String id, UserUpdateRequest request){
        User user=userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST));
        userMapper.updateUser(user,request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return  userMapper.toUserResponse(userRepository.save(user));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String id){
       userRepository.deleteById(id);
    }

    public void processVerify(String code){
        User user= userRepository.findByverificationCode(code).
                orElseThrow(()->new AppException(ErrorCode.VERIFYCATIONCODE_NOT_EXIST));
        user.setIsActive(true);
        userRepository.save(user);
    }

}
