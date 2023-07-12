package com.sparta.myblog.service;


import com.sparta.myblog.dto.PostRequestDto;
import com.sparta.myblog.dto.PostResponseDto;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.exception.ApiException;
import com.sparta.myblog.exception.ApiResult;
import com.sparta.myblog.repository.PostRepository;
import com.sparta.myblog.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, UserDetailsImpl userDetails) {

        Post post = new Post(requestDto, userDetails.getUser());

        postRepository.save(post);
        return new PostResponseDto(post);
    }

    public List<PostResponseDto> getAllPosts() {
        // 전체 조회
        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public PostResponseDto findPost(Long id) {
        Post post = findPostById(id);
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = findPostById(id);

        // 작성자가 일치하지 않거나 ADMIN이 아니면 던지기
        if (!post.getUser().getUsername().equals(userDetails.getUsername())
                && !userDetails.getRole().equals("ADMIN")) {
            throw new ApiException("작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }

        post.update(requestDto);
        return new PostResponseDto(post);
    }

    @Transactional
    public ApiResult deletePost(Long id, UserDetailsImpl userDetails) {
        Post post = findPostById(id);

        // 작성자가 일치하지 않거나 ADMIN이 아니면 던지기
        if (!post.getUser().getUsername().equals(userDetails.getUser().getUsername())
                && !userDetails.getRole().equals("ADMIN")) {
            throw new ApiException("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }

        // 게시글 삭제
        postRepository.delete(post);

        return new ApiResult("게시글 삭제 성공", HttpStatus.OK.value());
    }

    private Post findPostById(Long id) {
        // 선택한 게시글 존재 확인
        return postRepository.findById(id).orElseThrow(
                () -> new ApiException("선택한 게시글은 존재하지 않습니다.", HttpStatus.BAD_REQUEST)
        );
    }
}
