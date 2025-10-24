package com.ruoyi.yike.service.impl;

import com.ruoyi.yike.domain.UserFollow;
import com.ruoyi.yike.mapper.UserFollowMapper;
import com.ruoyi.yike.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/10/23下午 6:24
 * @Author : SoakLightDust
 */
@Service
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private UserFollowMapper userFollowMapper;

    /**
     * 查询用户关注
     *
     * @param id 用户关注主键
     * @return 用户关注
     */
    @Override
    public UserFollow selectUserFollowById(Long id) {
        return userFollowMapper.selectUserFollowById(id);
    }

    /**
     * 查询用户关注列表
     *
     * @param userFollow 用户关注
     * @return 用户关注
     */
    @Override
    public List<UserFollow> selectUserFollowList(UserFollow userFollow) {
        return userFollowMapper.selectUserFollowList(userFollow);
    }

    /**
     * 新增用户关注
     *
     * @param userFollow 用户关注
     * @return 结果
     */
    @Override
    public int insertUserFollow(UserFollow userFollow) {
        return userFollowMapper.insertUserFollow(userFollow);
    }

    /**
     * 修改用户关注
     *
     * @param userFollow 用户关注
     * @return 结果
     */
    @Override
    public int updateUserFollow(UserFollow userFollow) {
        return userFollowMapper.updateUserFollow(userFollow);
    }

    /**
     * 批量删除用户关注
     *
     * @param ids 需要删除的用户关注主键
     * @return 结果
     */
    @Override
    public int deleteUserFollowByIds(Long[] ids) {
        return userFollowMapper.deleteUserFollowByIds(ids);
    }

    /**
     * 删除用户关注信息
     *
     * @param id 用户关注主键
     * @return 结果
     */
    @Override
    public int deleteUserFollowById(Long id) {
        return userFollowMapper.deleteUserFollowById(id);
    }

    /**
     * 关注用户
     *
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 结果
     */
    @Override
    public int followUser(Long followerId, Long followingId) {
        // 检查是否已关注
        if (isFollowing(followerId, followingId)) {
            return 0;
        }

        UserFollow userFollow = new UserFollow();
        userFollow.setFollowerId(followerId);
        userFollow.setFollowingId(followingId);
        return userFollowMapper.insertUserFollow(userFollow);
    }

    /**
     * 取消关注用户
     *
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 结果
     */
    @Override
    public int unfollowUser(Long followerId, Long followingId) {
        return userFollowMapper.deleteByFollowerAndFollowing(followerId, followingId);
    }

    /**
     * 检查是否已关注
     *
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 是否已关注
     */
    @Override
    public boolean isFollowing(Long followerId, Long followingId) {
        int count = userFollowMapper.checkFollowExists(followerId, followingId);
        return count > 0;
    }

    /**
     * 获取用户关注数
     *
     * @param userId 用户ID
     * @return 关注数
     */
    @Override
    public int getFollowingCount(Long userId) {
        return userFollowMapper.countFollowing(userId);
    }

    /**
     * 获取用户粉丝数
     *
     * @param userId 用户ID
     * @return 粉丝数
     */
    @Override
    public int getFollowerCount(Long userId) {
        return userFollowMapper.countFollowers(userId);
    }
}