package com.sparta.myblog.service;

import com.sparta.myblog.dto.PasswordRequestDto;
import com.sparta.myblog.dto.ProfileRequestDto;
import com.sparta.myblog.dto.ProfileResponseDto;
import com.sparta.myblog.dto.SignupRequestDto;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.entity.UserRoleEnum;
import com.sparta.myblog.jwt.JwtUtil;
import com.sparta.myblog.repository.UserRepository;
import com.sparta.myblog.exception.ApiException;
import com.sparta.myblog.exception.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkId = userRepository.findByUsername(requestDto.getUsername());
        if (checkId.isPresent()) {
            throw new ApiException("중복된 ID 입니다.", HttpStatus.BAD_REQUEST);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(HttpServletRequest request) {
        User user = jwtUtil.checkToken(request); // 로그인 된 유저에 맞는 정보 담기

        return new ProfileResponseDto(user.getUsername(), user.getIntroduction()); // 해당 유저 정보 반환 / 메세지반환 필요없다고 하심
    }

    @Transactional
    public ApiResult checkPassword(PasswordRequestDto requestDto, HttpServletRequest request) {
        User user = jwtUtil.checkToken(request);

        // 비밀번호 확인
        if (!user.getPassword().equals(requestDto.getPassword())) {
            throw new ApiException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ApiResult("프로필 수정으로 넘어가기", HttpStatus.OK.value()); // 수정 페이지로 넘어가기 전 비밀번호 확인
    }

    @Transactional
    public ApiResult updateProfile(ProfileRequestDto requestDto, HttpServletRequest request) {
        User user = jwtUtil.checkToken(request); // 로그인 된 유저에 맞는 정보 담기

        user.update(requestDto); // 유저 정보 수정

        return new ApiResult("정보 수정 완료", HttpStatus.OK.value());
    }
}