package com.ruoyi.yike.domain;

import java.util.Date;

public class UserFavorite {
    /** 收藏ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 笔记ID */
    private Long noteId;

    /** 收藏时间，对应数据库 created_at */
    private Date createdAt;

    // 关联对象
    private Note note;

    // 构造函数
    public UserFavorite() {}

    // Getter 和 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getNoteId() { return noteId; }
    public void setNoteId(Long noteId) { this.noteId = noteId; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Note getNote() { return note; }
    public void setNote(Note note) { this.note = note; }
}
