package com.library.module.response;

/**
 * API 실패 응답 시 상세 에러 정보를 담는 DTO
 */
public record ErrorResponse(
        int errorCode,       // 비즈니스 로직에서 정의한 에러 코드
        String errorMessage  // 사용자에게 보여줄 에러 메시지

) {}
