package com.ruoyi.yike.domain;

import java.util.Date;

public class UserLike {
    /** 点赞ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 点赞类型: note-笔记, comment-评论 */
    private String targetType;

    /** 目标ID */
    private Long targetId;

    /** 点赞时间 */
    private Date createTime;

    // 构造函数
    public UserLike() {}

    // Getter 和 Setter 方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
