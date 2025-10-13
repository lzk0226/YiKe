package com.ruoyi.yike.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 用户端评论信息
 */
public class Comment {
    /** 评论ID */
    private Long id;

    /** 笔记ID */
    private Long noteId;

    /** 评论用户ID */
    private Long userId;

    /** 父评论ID(回复功能) */
    private Long parentId;

    /** 评论内容 */
    private String content;

    /** 评论点赞数 */
    private Integer likes;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /** 评论用户信息（用于关联查询） */
    private User user;

    // 构造函数
    public Comment() {}

    public Comment(Long id, Long noteId, Long userId, Long parentId, String content, Integer likes, User user) {
        this.id = id;
        this.noteId = noteId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = content;
        this.likes = likes;
        this.user = user;
    }

    // Getter 和 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 便捷方法：获取评论者信息（为了前端兼容性，同时提供author属性）
     * 前端可以通过 comment.author 访问用户信息
     */
    public User getAuthor() {
        return this.user;
    }

    public void setAuthor(User author) {
        this.user = author;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", noteId=" + noteId +
                ", userId=" + userId +
                ", parentId=" + parentId +
                ", content='" + content + '\'' +
                ", likes=" + likes +
                ", createTime=" + createTime +
                ", user=" + (user != null ? user.getNickname() : "null") +
                '}';
    }
}