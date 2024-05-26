package com.datnguyen.rem.exception;

import com.datnguyen.rem.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<String>> handlingAppException(AppException exception){
        ErrorCode errorCode=exception.getErrorCode();
        ApiResponse<String> apiResponse=ApiResponse.<String>builder().message(errorCode.getMessage()).build();
        return  ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<?> handlingValidation(MethodArgumentNotValidException exception){
        String enumKey=exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode=ErrorCode.valueOf(enumKey);
        ApiResponse<?> apiResponse=ApiResponse.builder().message(errorCode.getMessage()).build();
        return  ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<String>> handlingAccessDenied(AccessDeniedException accessDeniedException){
        ApiResponse<String> apiResponse=ApiResponse.<String>builder().
                message(ErrorCode.UN_AUTHORIZATION.getMessage()).build();
        return ResponseEntity.status(ErrorCode.UN_AUTHORIZATION.getStatusCode()).body(apiResponse);
    }
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<String>> handlingUncaughtchException(Exception exception){
        ApiResponse<String> apiResponse=ApiResponse.<String>builder()
                .message(ErrorCode.CATCH_EXCEPTION_FAIL.getMessage()).build();
        return  ResponseEntity.status(ErrorCode.CATCH_EXCEPTION_FAIL.getStatusCode()).body(apiResponse);
    }
}
