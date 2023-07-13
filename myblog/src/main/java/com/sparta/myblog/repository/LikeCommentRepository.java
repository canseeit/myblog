package com.sparta.myblog.repository;

import com.sparta.myblog.entity.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    LikeComment findByUserIdAndCommentId(Long id, Long commentId);

}
