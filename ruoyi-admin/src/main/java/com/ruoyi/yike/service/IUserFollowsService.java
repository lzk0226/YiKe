package com.ruoyi.yike.service;

import java.util.List;
import com.ruoyi.yike.domain.UserFollows;

/**
 * 用户关注Service接口
 * 
 * @author ruoyi
 * @date 2025-10-26
 */
public interface IUserFollowsService 
{
    /**
     * 查询用户关注
     * 
     * @param id 用户关注主键
     * @return 用户关注
     */
    public UserFollows selectUserFollowsById(Long id);

    /**
     * 查询用户关注列表
     * 
     * @param userFollows 用户关注
     * @return 用户关注集合
     */
    public List<UserFollows> selectUserFollowsList(UserFollows userFollows);

    /**
     * 新增用户关注
     * 
     * @param userFollows 用户关注
     * @return 结果
     */
    public int insertUserFollows(UserFollows userFollows);

    /**
     * 修改用户关注
     * 
     * @param userFollows 用户关注
     * @return 结果
     */
    public int updateUserFollows(UserFollows userFollows);

    /**
     * 批量删除用户关注
     * 
     * @param ids 需要删除的用户关注主键集合
     * @return 结果
     */
    public int deleteUserFollowsByIds(Long[] ids);

    /**
     * 删除用户关注信息
     * 
     * @param id 用户关注主键
     * @return 结果
     */
    public int deleteUserFollowsById(Long id);
}
