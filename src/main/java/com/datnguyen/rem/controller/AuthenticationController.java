package com.datnguyen.rem.controller;

import com.datnguyen.rem.dto.request.AuthenticationRequest;
import com.datnguyen.rem.dto.request.IntrospectRequest;
import com.datnguyen.rem.dto.request.LogoutRequest;
import com.datnguyen.rem.dto.response.ApiResponse;
import com.datnguyen.rem.dto.response.AuthenticationResponse;
import com.datnguyen.rem.dto.response.IntrospectResponse;
import com.datnguyen.rem.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws JOSEException {
        var result = authenticationService.authentication(request);
        return ApiResponse.<AuthenticationResponse>builder().
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

        @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<String>builder().message("Log out success").build();
    }
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result=authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().
                result(result).build();
    }
}
