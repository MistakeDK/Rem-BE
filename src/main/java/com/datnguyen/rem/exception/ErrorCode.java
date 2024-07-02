package com.datnguyen.rem.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    CATCH_EXCEPTION_FAIL(0,"Can not catch Exception",HttpStatus.INTERNAL_SERVER_ERROR),
    UN_AUTHORIZATION(1000,"don't have permission",HttpStatus.FORBIDDEN),
    UNAUTHENTICATED( 1001,"not authenticated",HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRE(1002,"Token has been expire",HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(1003,"Password is wrong",HttpStatus.BAD_REQUEST),
    USER_IS_BAN(1004,"user is ban",HttpStatus.BAD_REQUEST),
    VERIFY_CODE_NOT_VALID(1005,"Code is not valid",HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST( 1006,"User not found",HttpStatus.NOT_FOUND),
    USER_EXISTED(1007,"User is Existed",HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID( 1008,"password must be at least 8 characters",HttpStatus.BAD_REQUEST),
    NAME_INVALID(1009,"name must be at least 4 characters",HttpStatus.BAD_REQUEST),
    DOB_INVALID(10010,"must be 18 year old",HttpStatus.UNPROCESSABLE_ENTITY),
    TOKEN_IS_EMPTY(1011,"token is empty",HttpStatus.BAD_REQUEST),
    USERNAME_OR_PASSWORD_WRONG(1012,"Username or password wrong",HttpStatus.BAD_REQUEST),
    IMG_IS_EMPTY(1101,"img is empty",HttpStatus.UNPROCESSABLE_ENTITY),
    DESCRIPTION_IS_EMPTY(1102,"description is empty",HttpStatus.UNPROCESSABLE_ENTITY),
    PRICE_INVALID(1103,"Price must be greater than 10000",HttpStatus.UNPROCESSABLE_ENTITY),
    CATEGORY_EXISTED(1200,"Category is Existed",HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1201,"Category is not existed",HttpStatus.NOT_FOUND),
    PRODUCT_NOT_EXIST(1300,"Product is not exist",HttpStatus.NOT_FOUND),
    PROMOTION_NOT_EXIST(1301,"Promotion is not exist",HttpStatus.BAD_REQUEST),
    PROMOTION_EXIST(1302,"Promotion is existed",HttpStatus.BAD_REQUEST),
    PROMOTION_CODE_IS_EMPTY(1303,"Promotion code is empty",HttpStatus.BAD_REQUEST),
    PROMOTION_TYPE_IS_EMPTY(1304,"Promotion type is empty",HttpStatus.BAD_REQUEST),
    PROMOTION_VALUE_IS_EMPTY(1305,"Promotion value is empty",HttpStatus.BAD_REQUEST),
    PROMOTION_IS_NOT_ACTIVE(1306,"Promotion is not active",HttpStatus.BAD_REQUEST),
    REVIEW_OF_PRODUCT_AND_ORDER_EXISTED(1400,"Review of the order and product already exists",HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED_IN_ORDER(1401,"product is not existed in order",HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXISTED(1500,"Order Not Exist",HttpStatus.BAD_REQUEST),
    ORDER_NOT_CHANGE_STATUS(1501,"Order has been DELIVERED",HttpStatus.BAD_REQUEST);
    final long codeException;
    final String message;
    final HttpStatus statusCode;
}
