package com.library.module.common.handler;

import com.library.module.exception.BaseErrorCode;
import com.library.module.exception.CommonErrorCode;
import com.library.module.exception.CustomException;
import com.library.module.response.ApiResponse;
import com.library.module.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        BaseErrorCode errorCode = e.getBaseErrorCode();

        log.warn("[CustomException] {} : {}", e.getBaseErrorCode(), e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorResponse));
    }

    // MethodArgumentNotValidException: Body, Valid 검증
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("입력 값이 올바르지 않습니다.");

        log.warn("[MethodArgumentNotValid] {}", errorMessage);

        ErrorResponse errorResponse = new ErrorResponse(
                CommonErrorCode.INVALID_INPUT.getCode(),
                errorMessage
        );

        return ResponseEntity
                .status(CommonErrorCode.INVALID_INPUT.getStatus())
                .body(ApiResponse.error(errorResponse));
    }

    // ConstraintViolationException: 메서드 파라미터 검증
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException e) {

        String errorMessage = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .findFirst()
                .orElse("입력 값이 올바르지 않습니다.");

        log.warn("[ConstraintViolation] {}", errorMessage);

        /*String errorMessage = e.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("입력 값이 올바르지 않습니다.");*/

        ErrorResponse errorResponse = new ErrorResponse(
                CommonErrorCode.INVALID_INPUT.getCode(),
                errorMessage
        );

        return ResponseEntity
                .status(CommonErrorCode.INVALID_INPUT.getStatus())
                .body(ApiResponse.error(errorResponse));
    }

    // HttpMessageNotReadableException: Json 파싱 실패, 변환 실패 등
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleMessageNotReadable(HttpMessageNotReadableException e){

        log.warn("[NotReadable] {}", e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                CommonErrorCode.INVALID_INPUT.getCode(),
                "요청 JSON 형식이 올바르지 않습니다."
        );

        return ResponseEntity
                .status(CommonErrorCode.INVALID_INPUT.getStatus())
                .body(ApiResponse.error(errorResponse));
    }


    // RuntimeException 처리 (예상치 못한 에러)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException e) {

        log.error("[RuntimeException] {}", e.getMessage(), e);

        CommonErrorCode errorCode = CommonErrorCode.INTERNAL_ERROR;

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorResponse));
    }
}