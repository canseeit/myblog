package com.sparta.myblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResult {
    private String msg;
    private int statusCode;

    @Override
    public String toString() {
        return "ApiResult{" +
                "msg='" + msg + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}