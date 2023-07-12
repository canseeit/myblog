package com.sparta.myblog.controller;

import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.exception.ApiResult;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.LikePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikePostController {
    private final LikePostService likePostService;

    @PostMapping("/likepost/{postId}")
    public PostResponseDto likePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        return likePostService.likePost(userDetails, postId);
    }

    @DeleteMapping("/likepost/{postId}")
    public ResponseEntity<ApiResult> deleteLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        return likePostService.deleteLike(userDetails, postId);
    }
}
