package com.ruoyi.yike.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 笔记图片对象 note_images
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public class NoteImages extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 笔记ID */
    @Excel(name = "笔记ID")
    private Long noteId;

    /** 图片URL */
    @Excel(name = "图片URL")
    private String imageUrl;

    /** 图片顺序 */
    @Excel(name = "图片顺序")
    private Long imageOrder;

    /** 上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setNoteId(Long noteId) 
    {
        this.noteId = noteId;
    }

    public Long getNoteId() 
    {
        return noteId;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }

    public void setImageOrder(Long imageOrder) 
    {
        this.imageOrder = imageOrder;
    }

    public Long getImageOrder() 
    {
        return imageOrder;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("noteId", getNoteId())
            .append("imageUrl", getImageUrl())
            .append("imageOrder", getImageOrder())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
