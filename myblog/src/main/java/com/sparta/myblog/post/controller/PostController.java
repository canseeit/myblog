package com.sparta.myblog.post.controller;

import com.sparta.myblog.exception.ApiResult;
import com.sparta.myblog.post.dto.PostRequestDto;
import com.sparta.myblog.post.dto.PostResponseDto;
import com.sparta.myblog.post.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    @GetMapping("/posts")
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/post/{id}")
    public PostResponseDto findPost(@PathVariable Long id) {
        return postService.findPost(id);
    }

    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        return postService.updatePost(id, requestDto);
    }

    @DeleteMapping("/post/{id}")
    public ApiResult deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }
}