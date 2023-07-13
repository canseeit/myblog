package com.sparta.myblog.controller;

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

    @PostMapping("/like-post/{postId}")
    public ResponseEntity<ApiResult> likePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        return likePostService.likePost(userDetails, postId);
    }

    @DeleteMapping("/like-post/{postId}")
    public ResponseEntity<ApiResult> deleteLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        return likePostService.cancelLike(userDetails, postId);
    }
}
