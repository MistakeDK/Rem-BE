package com.datnguyen.rem.service.impl;

import com.datnguyen.rem.dto.request.ChangePasswordRequest;
import com.datnguyen.rem.dto.request.UserCreationRequest;
import com.datnguyen.rem.dto.request.UserUpdateRequest;
import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.dto.response.UserResponse;
import com.datnguyen.rem.entity.User;
import com.datnguyen.rem.enums.UserProvide;
import com.datnguyen.rem.exception.AppException;
import com.datnguyen.rem.exception.ErrorCode;
import com.datnguyen.rem.mapper.UserMapper;
import com.datnguyen.rem.repository.UserRepository;
import com.datnguyen.rem.repository.specification.GenericSpecificationBuilder;
import com.datnguyen.rem.service.UserService;
import com.datnguyen.rem.utils.SpecificationUtils;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;
import java.util.Objects;


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
        if(userRepository.existsByUsername(request.getUsername())||userRepository.existsByEmail(request.getEmail())){
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
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST)));
    }
    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getMyInfo(){
        var context= SecurityContextHolder.getContext();
        String name=context.getAuthentication().getName();
        return userMapper.toUserResponse(userRepository.findByUsername(name).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST)));
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
        User user=userRepository.findByUsername(userName)
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST));
        String verificationCode= baseRedisService.hashGet(userName,"verificationCode").toString();
        if(!Objects.equals(verificationCode, code)){
            throw new AppException(ErrorCode.VERIFY_CODE_NOT_VALID);
        }
        user.setIsActive(true);
        baseRedisService.delete(userName);
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasAuthority('USER')")
    @Transactional
    public void ChangePassword(ChangePasswordRequest request,String id){
        var user=userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST));
        boolean isMatch= passwordEncoder.matches(request.getOldPassword(), user.getPassword());
        if(!isMatch){
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public PageResponse<?> getList(Pageable pageable, String[] user) {
        Page<User> userPage=null;
        if(user!=null){
            GenericSpecificationBuilder<User> builder=new GenericSpecificationBuilder<>();
            SpecificationUtils.ConvertFormStringToSpecification(builder,user);
            userPage=userRepository.findAll(builder.build(User.class),pageable);
        }
        else {
            userPage=userRepository.findAll(pageable);
        }
        return PageResponse.builder()
                .pageNo(userPage.getNumber())
                .pageSize(userPage.getSize())
                .items(userPage.getContent().stream().map(userMapper::toUserResponse).toList())
                .totalItem(userPage.getTotalElements())
                .totalPage(userPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public void changeStatus(String email) {
        var user=userRepository.findByEmail(email).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST));
        user.setIsBan(!user.getIsBan());
    }
}
