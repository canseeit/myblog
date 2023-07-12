package com.sparta.myblog.controller;

import com.sparta.myblog.dto.PasswordRequestDto;
import com.sparta.myblog.dto.ProfileRequestDto;
import com.sparta.myblog.dto.ProfileResponseDto;
import com.sparta.myblog.dto.SignupRequestDto;
import com.sparta.myblog.exception.ApiResult;
import com.sparta.myblog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);
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