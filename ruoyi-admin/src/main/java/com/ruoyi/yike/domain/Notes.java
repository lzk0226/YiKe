package com.ruoyi.yike.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 笔记对象 notes
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public class Notes extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 笔记标题 */
    @Excel(name = "笔记标题")
    private String title;

    /** 笔记内容(富文本) */
    @Excel(name = "笔记内容(富文本)")
    private String content;

    /** 笔记简介 */
    @Excel(name = "笔记简介")
    private String description;

    /** 学科分类ID */
    @Excel(name = "学科分类ID")
    private Long subjectId;

    /** 笔记类型ID */
    @Excel(name = "笔记类型ID")
    private Long noteTypeId;

    /** 作者ID */
    @Excel(name = "作者ID")
    private Long userId;

    /** 浏览量 */
    @Excel(name = "浏览量")
    private Long views;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Long likes;

    /** 收藏数 */
    @Excel(name = "收藏数")
    private Long favorites;

    /** 评分(0-5分) */
    @Excel(name = "评分(0-5分)")
    private BigDecimal rating;

    /** 评分人数 */
    @Excel(name = "评分人数")
    private Long ratingCount;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;

    /** 状态: 1-正常, 0-下架 */
    @Excel(name = "状态: 1-正常, 0-下架")
    private Long status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setSubjectId(Long subjectId) 
    {
        this.subjectId = subjectId;
    }

    public Long getSubjectId() 
    {
        return subjectId;
    }

    public void setNoteTypeId(Long noteTypeId) 
    {
        this.noteTypeId = noteTypeId;
    }

    public Long getNoteTypeId() 
    {
        return noteTypeId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setViews(Long views) 
    {
        this.views = views;
    }

    public Long getViews() 
    {
        return views;
    }

    public void setLikes(Long likes) 
    {
        this.likes = likes;
    }

    public Long getLikes() 
    {
        return likes;
    }

    public void setFavorites(Long favorites) 
    {
        this.favorites = favorites;
    }

    public Long getFavorites() 
    {
        return favorites;
    }

    public void setRating(BigDecimal rating) 
    {
        this.rating = rating;
    }

    public BigDecimal getRating() 
    {
        return rating;
    }

    public void setRatingCount(Long ratingCount) 
    {
        this.ratingCount = ratingCount;
    }

    public Long getRatingCount() 
    {
        return ratingCount;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
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
            .append("title", getTitle())
            .append("content", getContent())
            .append("description", getDescription())
            .append("subjectId", getSubjectId())
            .append("noteTypeId", getNoteTypeId())
            .append("userId", getUserId())
            .append("views", getViews())
            .append("likes", getLikes())
            .append("favorites", getFavorites())
            .append("rating", getRating())
            .append("ratingCount", getRatingCount())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("status", getStatus())
            .toString();
    }
}
