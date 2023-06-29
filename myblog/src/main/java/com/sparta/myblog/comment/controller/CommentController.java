package com.sparta.myblog.comment.controller;

import com.sparta.myblog.comment.dto.CommentRequestDto;
import com.sparta.myblog.comment.dto.CommentResponseDto;
import com.sparta.myblog.comment.service.CommentService;
import com.sparta.myblog.exception.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }

    @PutMapping("/comment/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, request);
    }

    @DeleteMapping("/comment/{id}")
    public ApiResult deleteComment(@PathVariable Long id, HttpServletRequest request) {
        return commentService.deleteComment(id, request);
    }
}
