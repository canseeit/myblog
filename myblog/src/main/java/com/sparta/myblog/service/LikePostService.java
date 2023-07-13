package com.sparta.myblog.service;

import com.sparta.myblog.entity.LikePost;
import com.sparta.myblog.entity.Post;
import com.sparta.myblog.exception.ApiException;
import com.sparta.myblog.exception.ApiResult;
import com.sparta.myblog.repository.LikePostRepository;
import com.sparta.myblog.repository.PostRepository;
import com.sparta.myblog.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikePostService {
    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;

    @Transactional
    public ResponseEntity<ApiResult> likePost(UserDetailsImpl userDetails, Long postId) {
        // postId 게시글 가져오기
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // 작성자 본인의 게시글 좋아요 불가
        if (userDetails.getUser().getId().equals(post.getUser().getId())) {
            throw new IllegalArgumentException("본인의 게시글 좋아요 불가");
        }

        // 좋아요 중복 불가
        LikePost likePost = likePostRepository.findByUserIdAndPostId(userDetails.getUser().getId(), postId);
        if (likePost != null) {
            throw new IllegalArgumentException("좋아요 중복 불가");
        }

        likePostRepository.save(new LikePost(userDetails.getUser(), post));

        return ResponseEntity.ok().body(new ApiResult("좋아요 성공", HttpStatus.OK.value()));
    }

    @Transactional
    public ResponseEntity<ApiResult> cancelLike(UserDetailsImpl userDetails, Long postId) {
        LikePost likePost = likePostRepository.findByUserIdAndPostId(userDetails.getUser().getId(), postId);

        // 작성자가 일치하지 않거나 ADMIN이 아니면 던지기
        if (!likePost.getUser().getId().equals(userDetails.getUser().getId())
                && !userDetails.getRole().equals("ADMIN")) {
            throw new IllegalArgumentException("본인만 취소할 수 있습니다.");
        }

        likePostRepository.delete(likePost);

        return ResponseEntity.ok().body(new ApiResult("좋아요 취소 성공", HttpStatus.OK.value()));
    }
}
