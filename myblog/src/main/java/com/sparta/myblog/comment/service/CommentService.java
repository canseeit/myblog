package com.sparta.myblog.comment.service;

import com.sparta.myblog.comment.dto.CommentRequestDto;
import com.sparta.myblog.comment.dto.CommentResponseDto;
import com.sparta.myblog.comment.entity.Comment;
import com.sparta.myblog.comment.repository.CommentRepository;
import com.sparta.myblog.common.returnvalue.ApiException;
import com.sparta.myblog.common.returnvalue.ApiResult;
import com.sparta.myblog.common.jwt.JwtUtil;
import com.sparta.myblog.post.entity.Post;
import com.sparta.myblog.post.repository.PostRepository;
import com.sparta.myblog.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, HttpServletRequest request) {

        User user = jwtUtil.checkToken(request);

        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
                () -> new ApiException("게시글이 존재하지 않습니다.", HttpStatus.BAD_REQUEST)
        );

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(requestDto.getContent());

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        Comment comment = findCommentById(id);

        // 토큰 확인
        User user = jwtUtil.checkToken(request);

        // 작성자가 일치하지 않거나 ADMIN이 아니면 던지기
        if (!comment.getUser().getUsername().equals(user.getUsername()) && !user.getRole().equals("ADMIN")) {
            throw new ApiException("작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }

        comment.setContent(requestDto.getContent());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public ApiResult deleteComment(Long id, HttpServletRequest request) {
        Comment comment = findCommentById(id);

        // 토큰 확인
        User user = jwtUtil.checkToken(request);

        // 작성자가 일치하지 않거나 ADMIN이 아니면 던지기
        if (!comment.getUser().getUsername().equals(user.getUsername()) && !user.getRole().equals("ADMIN")) {
            throw new ApiException("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }

        commentRepository.delete(comment);

        return new ApiResult("댓글 삭제 성공", HttpStatus.OK.value());
    }

    private Comment findCommentById(Long id) {
        // 선택한 게시글 존재 확인
        return commentRepository.findById(id).orElseThrow(
                () ->
                        new ApiException("선택한 게시글은 존재하지 않습니다.", HttpStatus.BAD_REQUEST)
        );
    }
}
