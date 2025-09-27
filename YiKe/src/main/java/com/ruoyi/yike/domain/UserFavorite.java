package com.ruoyi.yike.domain;

import java.util.Date;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/9/26上午 10:40
 * @Author : SoakLightDust
 */
public class UserFavorite {
    /** 收藏ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 笔记ID */
    private Long noteId;

    /** 收藏时间 */
    private Date createTime;

    // 关联对象
    /** 收藏的笔记信息 */
    private Note note;

    // 构造函数
    public UserFavorite() {}

    // Getter 和 Setter 方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getNoteId() { return noteId; }
    public void setNoteId(Long noteId) { this.noteId = noteId; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Note getNote() { return note; }
    public void setNote(Note note) { this.note = note; }
}

