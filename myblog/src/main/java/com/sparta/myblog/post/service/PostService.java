package com.sparta.myblog.post.service;


import com.sparta.myblog.returnvalue.ApiException;
import com.sparta.myblog.returnvalue.ApiResult;
import com.sparta.myblog.jwt.JwtUtil;
import com.sparta.myblog.post.dto.PostRequestDto;
import com.sparta.myblog.post.dto.PostResponseDto;
import com.sparta.myblog.post.entity.Post;
import com.sparta.myblog.post.repository.PostRepository;
import com.sparta.myblog.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto) {

        User user = jwtUtil.checkToken(request);

        Post post = new Post(requestDto, user);

        postRepository.save(post);
        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
        // 전체 조회
        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public PostResponseDto findPost(Long id) {
        Post post = findPostById(id);
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto) {
        Post post = findPostById(id);
        // 토큰 체크
        User user = jwtUtil.checkToken(request);

        // 작성자가 일치하지 않거나 ADMIN이 아니면 던지기
        if (!post.getUser().getUsername().equals(user.getUsername()) && !user.getRole().equals("ADMIN")) {
            throw new ApiException("작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }

        post.update(requestDto);
        return new PostResponseDto(post);
    }

    @Transactional
    public ApiResult deletePost(Long id) {
        Post post = findPostById(id);
        // 토큰 체크
        User user = jwtUtil.checkToken(request);

        // 작성자가 일치하지 않거나 ADMIN이 아니면 던지기
        if (!post.getUser().getUsername().equals(user.getUsername()) && !user.getRole().equals("ADMIN")) {
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
