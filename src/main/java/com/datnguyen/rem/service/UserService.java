package com.datnguyen.rem.service;

import com.datnguyen.rem.dto.request.ChangePasswordRequest;
import com.datnguyen.rem.dto.request.UserCreationRequest;
import com.datnguyen.rem.dto.request.UserUpdateRequest;
import com.datnguyen.rem.dto.response.PageResponse;
import com.datnguyen.rem.dto.response.UserResponse;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request) throws MessagingException, IOException, TemplateException;
    
    UserResponse getUser(String id);
    UserResponse getMyInfo();
    UserResponse updateUser(String id, UserUpdateRequest request);
    void deleteUser(String id);
    void processVerify(String userName,String code);
    void ChangePassword(ChangePasswordRequest request, String id);

    PageResponse<?> getList(Pageable pageable, String[] user);

    void changeStatus(String email);
}
