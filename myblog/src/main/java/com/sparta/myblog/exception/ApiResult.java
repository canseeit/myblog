package com.sparta.myblog.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ApiResult {
    private String msg;
    private Integer statusCode;

    @Builder
    public ApiResult(String msg, Integer statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}