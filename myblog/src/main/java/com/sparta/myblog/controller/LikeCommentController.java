package com.sparta.myblog.controller;

import com.sparta.myblog.exception.ApiResult;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.service.LikeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeCommentController {
    private final LikeCommentService likeCommentService;

    @PostMapping("/like-comment/{commentId}")
    public ResponseEntity<ApiResult> likeComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId) {
        return likeCommentService.likeComment(userDetails, commentId);
    }

    @DeleteMapping("/like-comment/{commentId}")
    public ResponseEntity<ApiResult> cancelLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId) {
        return likeCommentService.cancelLike(userDetails, commentId);
    }
}
