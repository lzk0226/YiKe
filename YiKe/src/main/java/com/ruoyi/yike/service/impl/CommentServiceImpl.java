package com.ruoyi.yike.service.impl;

import com.ruoyi.yike.domain.Comment;
import com.ruoyi.yike.mapper.CommentMapper;
import com.ruoyi.yike.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getAllComments() {
        return commentMapper.selectAllComments();
    }

    @Override
    public List<Comment> getCommentsByNoteId(Long noteId) {
        return commentMapper.selectCommentsByNoteId(noteId);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentMapper.selectCommentById(id);
    }

    @Override
    public boolean addComment(Comment comment) {
        // 新增时默认点赞数为0
        if (comment.getLikes() == null) {
            comment.setLikes(0);
        }
        return commentMapper.insertComment(comment) > 0;
    }

    @Override
    public boolean likeComment(Long id) {
        Comment comment = commentMapper.selectCommentById(id);
        if (comment == null) return false;
        int newLikes = comment.getLikes() + 1;
        return commentMapper.updateCommentLikes(id, newLikes) > 0;
    }
}
