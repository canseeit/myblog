package com.sparta.myblog.exception;

import lombok.Getter;

@Getter
public class ApiException {
    private String msg;
    private int status;

    public ApiException(String message, int status) {
        this.msg = message;
        this.status = status;
    }
}