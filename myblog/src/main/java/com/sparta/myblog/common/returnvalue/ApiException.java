package com.sparta.myblog.common.returnvalue;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final String msg;
    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        this.msg = message;
        this.status = status;
    }
}