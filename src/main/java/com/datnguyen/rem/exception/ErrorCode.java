package com.datnguyen.rem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UN_AUTHORIZATION("don't have permission",HttpStatus.FORBIDDEN),
    UNAUTHENTICATED( "not authenticated",HttpStatus.UNAUTHORIZED),
    USER_IS_BAN("user is ban",HttpStatus.BAD_REQUEST),
    VERIFY_CODE_NOT_EXIST("code is not exist",HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST( "User not found",HttpStatus.NOT_FOUND),
    PASSWORD_INVALID( "password must be at least 8 characters",HttpStatus.BAD_REQUEST),
    NAME_INVALID("name must be at least 4 characters",HttpStatus.BAD_REQUEST),
    DOB_INVALID("must be 18 year old",HttpStatus.BAD_REQUEST),
    TOKEN_IS_EMPTY("token is empty",HttpStatus.BAD_REQUEST),
    IMG_IS_EMPTY("img is empty",HttpStatus.BAD_REQUEST),
    DESCRIPTION_IS_EMPTY("description is empty",HttpStatus.BAD_REQUEST),
    CATCH_EXCEPTION_FAIL("Can not catch Exception",HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED("User is Existed",HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED("Category is Existed",HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED("Category is not existed",HttpStatus.BAD_REQUEST),
    PRICE_INVALID("Price must be greater than 10000",HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXIST("Product is not exist",HttpStatus.BAD_REQUEST),
    PROMOTION_NOT_EXIST("Promotion is not exist",HttpStatus.BAD_REQUEST),
    PROMOTION_EXIST("Promotion is existed",HttpStatus.BAD_REQUEST),
    PROMOTION_CODE_IS_EMPTY("Promotion code is empty",HttpStatus.BAD_REQUEST),
    PROMOTION_TYPE_IS_EMPTY("Promotion type is empty",HttpStatus.BAD_REQUEST),
    PROMOTION_VALUE_IS_EMPTY("Promotion value is empty",HttpStatus.BAD_REQUEST);
    final String message;
    final HttpStatus statusCode;
}
