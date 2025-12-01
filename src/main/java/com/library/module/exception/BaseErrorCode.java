package com.library.module.exception;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getStatus();
    String getMessage();
    int getCode();
}
