package com.library.module.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements BaseErrorCode{

    /**
     * 공통 error -> 1000번대
     */
    INTERNAL_ERROR(1000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    INVALID_INPUT(1001, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED(1002, HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    FORBIDDEN(1003, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    NOT_FOUND(1004, HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."),






    ;

    private final int code;
    private final HttpStatus status;
    private final String message;
}
