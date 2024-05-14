package com.datnguyen.rem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNAUTHORIZATION("don't have permission",HttpStatus.FORBIDDEN),
    UNAUTHENTICATED( "not authenticated",HttpStatus.UNAUTHORIZED),
    USER_NOT_EXIST( "User not found",HttpStatus.NOT_FOUND),
    PASSWORD_INVALID( "password is not valid",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID("username is not valid",HttpStatus.BAD_REQUEST),
    DOB_INVALID("must be {min} year old",HttpStatus.BAD_REQUEST),
    CATCH_EXCEPTION_FAIL("Can not catch Exception",HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED("User is Existed",HttpStatus.BAD_REQUEST);
    final String message;
    final HttpStatus statusCode;
}
