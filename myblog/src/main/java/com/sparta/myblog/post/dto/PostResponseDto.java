package com.sparta.myblog.post.dto;

import com.sparta.myblog.comment.dto.CommentResponseDto;
import com.sparta.myblog.comment.entity.Comment;
import com.sparta.myblog.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getUsername();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.commentList = new ArrayList<>();
        for (Comment comment : post.getCommentList()) {
            CommentResponseDto commentDto = new CommentResponseDto(comment);
            this.commentList.add(commentDto);
        }
    }
}