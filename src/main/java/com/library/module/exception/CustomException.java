package com.library.module.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final BaseErrorCode baseErrorCode;

    public CustomException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode.getMessage());
        this.baseErrorCode = baseErrorCode;
    }
}