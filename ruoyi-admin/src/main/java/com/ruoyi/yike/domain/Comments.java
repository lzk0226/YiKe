package com.ruoyi.yike.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 评论对象 comments
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public class Comments extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 笔记ID */
    @Excel(name = "笔记ID")
    private Long noteId;

    /** 评论用户ID */
    @Excel(name = "评论用户ID")
    private Long userId;

    /** 父评论ID(回复功能) */
    @Excel(name = "父评论ID(回复功能)")
    private Long parentId;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    /** 评论点赞数 */
    @Excel(name = "评论点赞数")
    private Long likes;

    /** 评论时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "评论时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 状态: 1-正常, 0-删除 */
    @Excel(name = "状态: 1-正常, 0-删除")
    private Long status;

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

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setLikes(Long likes) 
    {
        this.likes = likes;
    }

    public Long getLikes() 
    {
        return likes;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("noteId", getNoteId())
            .append("userId", getUserId())
            .append("parentId", getParentId())
            .append("content", getContent())
            .append("likes", getLikes())
            .append("createdAt", getCreatedAt())
            .append("status", getStatus())
            .toString();
    }
}
