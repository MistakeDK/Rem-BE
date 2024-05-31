package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.AuthenticationRequest;
import com.datnguyen.rem.dto.request.IntrospectRequest;
import com.datnguyen.rem.dto.request.LogoutRequest;
import com.datnguyen.rem.dto.request.RefreshRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.dto.response.AuthenticationResponse;
import com.datnguyen.rem.dto.response.IntrospectResponse;
import com.datnguyen.rem.service.AuthenticationService;
import com.datnguyen.rem.service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationController {
    @Value("${dev.site}")
    @NonFinal
    String url;
    AuthenticationService authenticationService;
    UserService userService;
    @PostMapping("/logIn")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws JOSEException {
        var result = authenticationService.authentication(request);
        return ApiResponse.<AuthenticationResponse>builder().
                result(result).build();
    }
    @GetMapping("/LogInWithGithub")
    void authenticateWithGithub(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @AuthenticationPrincipal OAuth2User principal) throws JOSEException, IOException {
        userService.createUserWithGithub(principal);
        response.sendRedirect(url+"/Authorization?name="+principal.getName());
    }
    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<String>builder().message("Log out success").build();
    }
    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> logout(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result= authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }


    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result=authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().
                result(result).build();
    }
    @PostMapping("/cookie")
    ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request,
                                                                     HttpServletResponse response)
            throws JOSEException {
        var result = authenticationService.authentication(request);
        Cookie cookie = new Cookie("token", result.getToken());
        cookie.setHttpOnly(true); // Để tránh truy cập từ JavaScript
        cookie.setSecure(true);   // Để sử dụng qua HTTPS
        cookie.setPath("/");      // Để cookie có sẵn cho toàn bộ ứng dụng
        cookie.setMaxAge(60); // Cookie có thời hạn 60s
        response.addCookie(cookie);
        ApiResponse<AuthenticationResponse> apiResponse=ApiResponse.<AuthenticationResponse>builder()
                .result(result).build();
        return ResponseEntity.ok(apiResponse);
    }

}
