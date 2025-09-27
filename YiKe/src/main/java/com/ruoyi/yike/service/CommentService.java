package com.ruoyi.yike.service;

import com.ruoyi.yike.domain.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllComments();

    List<Comment> getCommentsByNoteId(Long noteId);

    Comment getCommentById(Long id);

    boolean addComment(Comment comment);

    boolean likeComment(Long id);
}
