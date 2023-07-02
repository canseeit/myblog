package com.sparta.myblog.user.controller;

import com.sparta.myblog.common.returnvalue.ApiResult;
import com.sparta.myblog.user.dto.*;
import com.sparta.myblog.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResult signup(@RequestBody @Valid SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public ApiResult login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return userService.login(loginRequestDto, response);
    }

    @GetMapping("/profile")
    public ProfileResponseDto getProfile(HttpServletRequest request) {
        return userService.getProfile(request);
    }

    @PostMapping("/profile")
    public ApiResult checkPassword(@RequestBody PasswordRequestDto requestDto, HttpServletRequest request) {
        return userService.checkPassword(requestDto, request);
    }

    @PutMapping("/profile")
    public ApiResult updateProfile(@RequestBody ProfileRequestDto requestDto, HttpServletRequest request) {
        return userService.updateProfile(requestDto, request);
    }
}