package com.ruoyi.yike.domain;

import java.util.Date;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/10/23下午 6:20
 * @Author : SoakLightDust
 */
public class UserFollow {
    /** 关注ID */
    private Long id;

    /** 关注者ID */
    private Long followerId;

    /** 被关注者ID */
    private Long followingId;

    /** 关注时间，对应数据库 created_at */
    private Date createdAt;

    // 关联对象 - 被关注的用户
    private User followingUser;

    // 关联对象 - 关注者
    private User followerUser;

    // 构造函数
    public UserFollow() {}

    // Getter 和 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFollowerId() { return followerId; }
    public void setFollowerId(Long followerId) { this.followerId = followerId; }

    public Long getFollowingId() { return followingId; }
    public void setFollowingId(Long followingId) { this.followingId = followingId; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public User getFollowingUser() { return followingUser; }
    public void setFollowingUser(User followingUser) { this.followingUser = followingUser; }

    public User getFollowerUser() { return followerUser; }
    public void setFollowerUser(User followerUser) { this.followerUser = followerUser; }
}