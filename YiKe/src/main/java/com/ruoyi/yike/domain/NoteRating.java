package com.ruoyi.yike.domain;

import java.util.Date;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/9/26上午 10:41
 * @Author : SoakLightDust
 */
public class NoteRating {
    /** 评分ID */
    private Long id;

    /** 笔记ID */
    private Long noteId;

    /** 评分用户ID */
    private Long userId;

    /** 评分(1-5分) */
    private Integer rating;

    /** 评分时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    // 构造函数
    public NoteRating() {}

    // Getter 和 Setter 方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getNoteId() { return noteId; }
    public void setNoteId(Long noteId) { this.noteId = noteId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
