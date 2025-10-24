package com.ruoyi.yike.service;

import com.ruoyi.yike.domain.UserFollow;

import java.util.List;

public interface UserFollowService {
    /**
     * 查询用户关注
     *
     * @param id 用户关注主键
     * @return 用户关注
     */
    public UserFollow selectUserFollowById(Long id);

    /**
     * 查询用户关注列表
     *
     * @param userFollow 用户关注
     * @return 用户关注集合
     */
    public List<UserFollow> selectUserFollowList(UserFollow userFollow);

    /**
     * 新增用户关注
     *
     * @param userFollow 用户关注
     * @return 结果
     */
    public int insertUserFollow(UserFollow userFollow);

    /**
     * 修改用户关注
     *
     * @param userFollow 用户关注
     * @return 结果
     */
    public int updateUserFollow(UserFollow userFollow);

    /**
     * 批量删除用户关注
     *
     * @param ids 需要删除的用户关注主键集合
     * @return 结果
     */
    public int deleteUserFollowByIds(Long[] ids);

    /**
     * 删除用户关注信息
     *
     * @param id 用户关注主键
     * @return 结果
     */
    public int deleteUserFollowById(Long id);

    /**
     * 关注用户
     *
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 结果
     */
    public int followUser(Long followerId, Long followingId);

    /**
     * 取消关注用户
     *
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 结果
     */
    public int unfollowUser(Long followerId, Long followingId);

    /**
     * 检查是否已关注
     *
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否已关注
     */
    public boolean isFollowing(Long followerId, Long followingId);

    /**
     * 获取用户关注数
     *
     * @param userId 用户ID
     * @return 关注数
     */
    public int getFollowingCount(Long userId);

    /**
     * 获取用户粉丝数
     *
     * @param userId 用户ID
     * @return 粉丝数
     */
    public int getFollowerCount(Long userId);
}