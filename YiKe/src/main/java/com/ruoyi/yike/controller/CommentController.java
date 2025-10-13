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
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取所有评论
     * 返回包含用户信息的评论列表
     */
    @GetMapping("/all")
    public List<Comment> allComments() {
        List<Comment> comments = commentService.getAllComments();
        System.out.println("查询到评论数量: " + comments.size());

        // 打印评论信息，用于调试
        for (Comment comment : comments) {
            System.out.println("评论ID: " + comment.getId() +
                    ", 用户: " + (comment.getUser() != null ? comment.getUser().getNickname() : "null"));
        }

        return comments;
    }

    /**
     * 根据笔记ID获取评论
     * 返回包含用户信息的评论列表
     */
    @GetMapping("/note/{noteId}")
    public List<Comment> commentsByNote(@PathVariable Long noteId) {
        System.out.println("查询笔记ID为 " + noteId + " 的评论");

        List<Comment> comments = commentService.getCommentsByNoteId(noteId);
        System.out.println("查询到评论数量: " + comments.size());

        // 打印评论信息，用于调试
        for (Comment comment : comments) {
            if (comment.getUser() != null) {
                System.out.println("评论ID: " + comment.getId() +
                        ", 用户昵称: " + comment.getUser().getNickname() +
                        ", 用户头像: " + (comment.getUser().getAvatar() != null ? "有" : "无"));
            } else {
                System.out.println("评论ID: " + comment.getId() + ", 用户信息为null");
            }
        }

        return comments;
    }

    /**
     * 新增评论
     * @param comment 评论信息（至少包含noteId、userId、content）
     */
    @PostMapping("/add")
    public Map<String, Object> addComment(@RequestBody Comment comment) {
        System.out.println("收到新增评论请求: " + comment);

        boolean success = commentService.addComment(comment);
        Map<String, Object> result = new HashMap<>();

        if (success) {
            result.put("code", 200);
            result.put("message", "评论成功");
            result.put("data", comment); // 返回新增的评论（包含自动生成的ID）
        } else {
            result.put("code", 500);
            result.put("message", "评论失败");
        }

        return result;
    }

    /**
     * 点赞评论
     * @param id 评论ID
     */
    @PostMapping("/like/{id}")
    public Map<String, Object> likeComment(@PathVariable Long id) {
        System.out.println("收到评论点赞请求，评论ID: " + id);

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