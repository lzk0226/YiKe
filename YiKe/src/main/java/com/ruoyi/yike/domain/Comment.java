package com.ruoyi.yike.domain;

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

    /** 评论用户信息 */
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
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getNoteId() { return noteId; }
    public void setNoteId(Long noteId) { this.noteId = noteId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getLikes() { return likes; }
    public void setLikes(Integer likes) { this.likes = likes; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
