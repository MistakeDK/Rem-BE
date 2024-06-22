package com.datnguyen.rem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UN_AUTHORIZATION("don't have permission",HttpStatus.FORBIDDEN),
    UNAUTHENTICATED( "not authenticated",HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRE("Token has been expire",HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH("Password is wrong",HttpStatus.BAD_REQUEST),
    USER_IS_BAN("user is ban",HttpStatus.BAD_REQUEST),
    VERIFY_CODE_NOT_VALID("Code is not valid",HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST( "User not found",HttpStatus.NOT_FOUND),
    PASSWORD_INVALID( "password must be at least 8 characters",HttpStatus.BAD_REQUEST),
    NAME_INVALID("name must be at least 4 characters",HttpStatus.BAD_REQUEST),
    DOB_INVALID("must be 18 year old",HttpStatus.UNPROCESSABLE_ENTITY),
    TOKEN_IS_EMPTY("token is empty",HttpStatus.BAD_REQUEST),
    IMG_IS_EMPTY("img is empty",HttpStatus.UNPROCESSABLE_ENTITY),
    DESCRIPTION_IS_EMPTY("description is empty",HttpStatus.UNPROCESSABLE_ENTITY),
    CATCH_EXCEPTION_FAIL("Can not catch Exception",HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED("User is Existed",HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED("Category is Existed",HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED("Category is not existed",HttpStatus.NOT_FOUND),
    PRICE_INVALID("Price must be greater than 10000",HttpStatus.UNPROCESSABLE_ENTITY),
    PRODUCT_NOT_EXIST("Product is not exist",HttpStatus.NOT_FOUND),
    PROMOTION_NOT_EXIST("Promotion is not exist",HttpStatus.BAD_REQUEST),
    PROMOTION_EXIST("Promotion is existed",HttpStatus.BAD_REQUEST),
    PROMOTION_CODE_IS_EMPTY("Promotion code is empty",HttpStatus.BAD_REQUEST),
    PROMOTION_TYPE_IS_EMPTY("Promotion type is empty",HttpStatus.BAD_REQUEST),
    PROMOTION_VALUE_IS_EMPTY("Promotion value is empty",HttpStatus.BAD_REQUEST),
    REVIEW_OF_PRODUCT_AND_ORDER_EXISTED("Review of the order and product already exists",HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED_IN_ORDER("product is not existed in order",HttpStatus.NOT_FOUND);
    final String message;
    final HttpStatus statusCode;
}
