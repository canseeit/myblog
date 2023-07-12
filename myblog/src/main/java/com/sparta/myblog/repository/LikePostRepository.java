package com.sparta.myblog.repository;

import com.sparta.myblog.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    LikePost findByUserIdAndPostId(Long id, Long postId);
}
