package com.sparta.myblog.controller;

import com.sparta.myblog.dto.PasswordRequestDto;
import com.sparta.myblog.dto.ProfileRequestDto;
import com.sparta.myblog.dto.ProfileResponseDto;
import com.sparta.myblog.dto.SignupRequestDto;
import com.sparta.myblog.exception.ApiResult;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResult> signup(@RequestBody @Valid SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok().body(new ApiResult("회원가입 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/profile")
    public ProfileResponseDto getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getProfile(userDetails);
    }

    @PostMapping("/profile")
    public ApiResult checkPassword(@RequestBody PasswordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.checkPassword(requestDto, userDetails);
    }

    @PutMapping("/profile")
    public ApiResult updateProfile(@RequestBody ProfileRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateProfile(requestDto, userDetails);
    }
}