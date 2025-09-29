package com.ruoyi.yike.domain;

import java.util.Date;

public class NoteImage {
    /** 图片ID */
    private Long id;

    /** 笔记ID */
    private Long noteId;

    /** 图片URL */
    private String imageUrl;

    /** 图片顺序 */
    private Integer imageOrder;

    /** 上传时间 */
    private Date createTime;

    // 构造函数
    public NoteImage() {}

    // Getter 和 Setter 方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getNoteId() { return noteId; }
    public void setNoteId(Long noteId) { this.noteId = noteId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getImageOrder() { return imageOrder; }
    public void setImageOrder(Integer imageOrder) { this.imageOrder = imageOrder; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
