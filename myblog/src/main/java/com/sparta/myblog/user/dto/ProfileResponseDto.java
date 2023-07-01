package com.sparta.myblog.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public class ProfileResponseDto {
    private String username;
    private String introduction;

    public ProfileResponseDto(String username, String introduction) {
        this.username = username;
        this.introduction = introduction;
    }
}
