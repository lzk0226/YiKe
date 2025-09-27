package com.ruoyi.yike.controller;

import com.ruoyi.yike.domain.Comment;
import com.ruoyi.yike.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /** 获取所有评论 */
    @GetMapping("/all")
    public List<Comment> allComments() {
        return commentService.getAllComments();
    }

    /** 根据笔记ID获取评论 */
    @GetMapping("/note/{noteId}")
    public List<Comment> commentsByNote(@PathVariable Long noteId) {
        return commentService.getCommentsByNoteId(noteId);
    }

    /** 新增评论 */
    @PostMapping("/add")
    public Map<String, Object> addComment(@RequestBody Comment comment) {
        boolean success = commentService.addComment(comment);
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("message", "评论成功");
        } else {
            result.put("code", 500);
            result.put("message", "评论失败");
        }
        return result;
    }

    /** 点赞评论 */
    @PostMapping("/like/{id}")
    public Map<String, Object> likeComment(@PathVariable Long id) {
        boolean success = commentService.likeComment(id);
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("message", "点赞成功");
        } else {
            result.put("code", 500);
            result.put("message", "点赞失败");
        }
        return result;
    }
}
