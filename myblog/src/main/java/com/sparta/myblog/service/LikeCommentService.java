package com.sparta.myblog.service;

import com.sparta.myblog.entity.Comment;
import com.sparta.myblog.entity.LikeComment;
import com.sparta.myblog.exception.ApiException;
import com.sparta.myblog.exception.ApiResult;
import com.sparta.myblog.repository.CommentRepository;
import com.sparta.myblog.repository.LikeCommentRepository;
import com.sparta.myblog.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeCommentService {
    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;

    @Transactional
    public ResponseEntity<ApiResult> likeComment(UserDetailsImpl userDetails, Long commentId) {
        // commentId 댓글 가져오기
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        // 작성자 본인의 댓글 좋아요 불가
        if (userDetails.getUser().getId().equals(comment.getUser().getId())) {
            throw new IllegalArgumentException("본인의 댓글 좋아요 불가");
        }

        // 좋아요 중복 불가
        LikeComment likeComment = likeCommentRepository.findByUserIdAndCommentId(userDetails.getUser().getId(), commentId);
        if (likeComment != null) {
            throw new IllegalArgumentException("좋아요 중복 불가");
        }

        likeCommentRepository.save(new LikeComment(userDetails.getUser(), comment));

        return ResponseEntity.ok().body(new ApiResult("좋아요 성공", HttpStatus.OK.value()));
    }

    @Transactional
    public ResponseEntity<ApiResult> cancelLike(UserDetailsImpl userDetails, Long commentId) {
        LikeComment likeComment = likeCommentRepository.findByUserIdAndCommentId(userDetails.getUser().getId(), commentId);

        // 작성자가 일치하지 않거나 ADMIN이 아니면 던지기
        if (!likeComment.getUser().getId().equals(userDetails.getUser().getId())
                && !userDetails.getRole().equals("ADMIN")) {
//            throw new ApiException("본인만 취소할 수 있습니다.", HttpStatus.BAD_REQUEST.value());
            throw new IllegalArgumentException("본인만 취소할 수 있습니다.");
        }

        likeCommentRepository.delete(likeComment);

        return ResponseEntity.ok().body(new ApiResult("좋아요 취소 성공", HttpStatus.OK.value()));
    }
}
