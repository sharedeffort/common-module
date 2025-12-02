package com.library.module.common.handler;

import com.library.module.exception.BaseErrorCode;
import com.library.module.exception.CommonErrorCode;
import com.library.module.exception.CustomException;
import com.library.module.response.ApiResponse;
import com.library.module.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException ex) {
        BaseErrorCode errorCode = ex.getBaseErrorCode();

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage()
        );

        return new ResponseEntity<>(
                ApiResponse.error(errorResponse),
                errorCode.getStatus()
        );
    }

    // MethodArgumentNotValidException Body, Valid 검증
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst()
                .orElse("입력 값이 올바르지 않습니다.");

        ErrorResponse errorResponse = new ErrorResponse(
                CommonErrorCode.INVALID_INPUT.getCode(),
                errorMessage
        );

        return new ResponseEntity<>(
                ApiResponse.error(errorResponse),
                CommonErrorCode.INVALID_INPUT.getStatus()
        );
    }

    // ConstraintViolationException 메서드 파라미터 검증
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException e) {

        String errorMessage = e.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("입력 값이 올바르지 않습니다.");

        ErrorResponse errorResponse = new ErrorResponse(
                CommonErrorCode.INVALID_INPUT.getCode(),
                errorMessage
        );

        return new ResponseEntity<>(
                ApiResponse.error(errorResponse),
                CommonErrorCode.INVALID_INPUT.getStatus()
        );
    }

    // RuntimeException 처리 (예상치 못한 에러)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException ex) {
        CommonErrorCode errorCode = CommonErrorCode.INTERNAL_ERROR;

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage()
        );

        return new ResponseEntity<>(
                ApiResponse.error(errorResponse),
                errorCode.getStatus()
        );
    }
}