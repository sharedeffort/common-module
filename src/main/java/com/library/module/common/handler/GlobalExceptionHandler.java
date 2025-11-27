package com.library.module.common.handler;

import com.library.module.exception.CustomException;
import com.library.module.response.ApiResponse;
import com.library.module.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException이 발생했을 때 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {

        ErrorResponse errorBody = new ErrorResponse(e.getErrorCode(), e.getErrorMessage());

        ApiResponse<?> apiResponse = ApiResponse.error(errorBody,"응답 실패");

        // CustomException에 정의된 HTTP 상태 코드를 사용하여 응답
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(apiResponse);
    }

    // 예상치 못한 시스템 예외 처리 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception e) {

        // 5000: 공통 시스템 오류 코드
        ErrorResponse errorBody = new ErrorResponse(5000, "시스템 내부 오류가 발생했습니다.");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(errorBody));
    }
}
