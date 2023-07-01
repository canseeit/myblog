package com.sparta.myblog.user.service;

import com.sparta.myblog.returnvalue.ApiException;
import com.sparta.myblog.returnvalue.ApiResult;
import com.sparta.myblog.jwt.JwtUtil;
import com.sparta.myblog.user.dto.LoginRequestDto;
import com.sparta.myblog.user.dto.ProfileRequestDto;
import com.sparta.myblog.user.dto.ProfileResponseDto;
import com.sparta.myblog.user.dto.SignupRequestDto;
import com.sparta.myblog.user.entity.User;
import com.sparta.myblog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    @Transactional
    public ApiResult signup(SignupRequestDto requestDto) {

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(requestDto.getUsername());
        if (checkUsername.isPresent()) {
            throw new ApiException("중복된 username 입니다.", HttpStatus.BAD_REQUEST);
        }

        // 사용자 등록
        User user = new User(requestDto.getId(), requestDto.getUsername(), requestDto.getPassword(), requestDto.getRole());
        userRepository.save(user);

        return new ApiResult("회원가입 성공", HttpStatus.OK.value());
    }

    @Transactional
    public ApiResult login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ApiException("사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST)
        );

        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            throw new ApiException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        // JWT Token 생성 및 반환
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));

        return new ApiResult("로그인 성공", HttpStatus.OK.value());
    }

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(HttpServletRequest request) {

        User user = jwtUtil.checkToken(request); // 로그인 된 유저의 토큰에 맞는 정보 담기

        return new ProfileResponseDto(user.getUsername(), user.getIntroduction());
    }

    @Transactional
    public ApiResult updateProfile(ProfileRequestDto requestDto, HttpServletRequest request) {

        User user = jwtUtil.checkToken(request); // 로그인 된 유저의 토큰에 맞는 정보 담기

        user.update(requestDto); // 유저 정보 수정

        return new ApiResult("정보 수정 완료", HttpStatus.OK.value());
    }
}