package com.sparta.myblog.service;

import com.sparta.myblog.dto.CommentRequestDto;
import com.sparta.myblog.dto.CommentResponseDto;
import com.sparta.myblog.entity.Comment;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.exception.ApiException;
import com.sparta.myblog.exception.ApiResult;
import com.sparta.myblog.repository.CommentRepository;
import com.sparta.myblog.repository.PostRepository;
import com.sparta.myblog.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDto createComment(CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(
//                () -> new ApiException("게시글이 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value())
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        Comment comment = new Comment();
        comment.setUser(userDetails.getUser());
        comment.setPost(post);
        comment.setContent(requestDto.getContent());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Comment comment = findCommentById(id);

        // 작성자가 일치하지 않거나 ADMIN이 아니면 던지기
        if (!comment.getUser().getUsername().equals(userDetails.getUsername())
                && !userDetails.getRole().equals("ADMIN")) {
//            throw new ApiException("작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value());
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        comment.setContent(requestDto.getContent());
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public ApiResult deleteComment(Long id, UserDetailsImpl userDetails) {
        Comment comment = findCommentById(id);

        // 작성자가 일치하지 않거나 ADMIN이 아니면 던지기
        if (!comment.getUser().getUsername().equals(userDetails.getUsername())
                && !userDetails.getRole().equals("ADMIN")) {
//            throw new ApiException("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST.value());
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);

        return new ApiResult("댓글 삭제 성공", HttpStatus.OK.value());
    }

    private Comment findCommentById(Long id) {
        // 선택한 댓글 존재 확인
        return commentRepository.findById(id).orElseThrow(
                () ->
//                        new ApiException("선택한 댓글은 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value())
                        new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
    }
}
