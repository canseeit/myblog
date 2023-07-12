package com.sparta.myblog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleApiException(ApiException ex) {
        String msg = ex.getMsg();
        HttpStatus status = ex.getStatus();
        return new ResponseEntity<>(msg, status);
    }

    // 기본 예외 처리
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException() {
//        String msg = "서버 오류가 발생했습니다.";
//        return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}