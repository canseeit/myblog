package com.sparta.myblog.service;

import com.sparta.myblog.dto.PostResponseDto;
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
    private final LikePostRepository likePostRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostResponseDto likePost(UserDetailsImpl userDetails, Long postId) {
        Post post = findById(postId);
        // 작성자 본인의 게시글 좋아요 불가
        if (userDetails.getUser().getId().equals(post.getUser().getId())) {
            throw new IllegalArgumentException("본인의 게시글 좋아요 불가");
        }
        // 좋아요 중복 불가
        LikePost likePost = likePostRepository.findByUserIdAndPostId(userDetails.getUser().getId(), postId);
        if (likePost != null) {
            throw new IllegalArgumentException("좋아요 중복 불가");
        }

        likePostRepository.save(new LikePost(userDetails.getUser(), post)); // 좋아요 저장
        return new PostResponseDto(post);
    }

    @Transactional
    public ResponseEntity<ApiResult> deleteLike(UserDetailsImpl userDetails, Long postId) {
        Post post = findById(postId);
        LikePost likePost = likePostRepository.findByUserIdAndPostId(userDetails.getUser().getId(), postId);
        // 작성자가 일치하지 않거나 ADMIN이 아니면 던지기
        if (!likePost.getUser().getId().equals(userDetails.getUser().getId())
                && !userDetails.getRole().equals("ADMIN")) {
            throw new ApiException("본인만 취소할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }
//        likePostRepository.delete(new LikePost(userDetails.getUser(), post));
        likePostRepository.delete(likePost);

        return ResponseEntity.ok().body(new ApiResult("좋아요 취소 성공", HttpStatus.OK.value()));
    }

    public Post findById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        return post;
    }
}
