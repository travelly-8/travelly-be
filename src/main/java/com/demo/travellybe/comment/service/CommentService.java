package com.demo.travellybe.comment.service;

public interface CommentService {
    void saveComment(String content, Long reviewId, Long commentId, String username);
}
