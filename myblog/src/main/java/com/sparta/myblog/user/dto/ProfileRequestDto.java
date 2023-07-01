package com.sparta.myblog.user.dto;

import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private String username;
    private String password;
    private String introduction;

}
