package com.ruoyi.yike.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.yike.mapper.UserFollowsMapper;
import com.ruoyi.yike.domain.UserFollows;
import com.ruoyi.yike.service.IUserFollowsService;

/**
 * 用户关注Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-26
 */
@Service
public class UserFollowsServiceImpl implements IUserFollowsService 
{
    @Autowired
    private UserFollowsMapper userFollowsMapper;

    /**
     * 查询用户关注
     * 
     * @param id 用户关注主键
     * @return 用户关注
     */
    @Override
    public UserFollows selectUserFollowsById(Long id)
    {
        return userFollowsMapper.selectUserFollowsById(id);
    }

    /**
     * 查询用户关注列表
     * 
     * @param userFollows 用户关注
     * @return 用户关注
     */
    @Override
    public List<UserFollows> selectUserFollowsList(UserFollows userFollows)
    {
        return userFollowsMapper.selectUserFollowsList(userFollows);
    }

    /**
     * 新增用户关注
     * 
     * @param userFollows 用户关注
     * @return 结果
     */
    @Override
    public int insertUserFollows(UserFollows userFollows)
    {
        return userFollowsMapper.insertUserFollows(userFollows);
    }

    /**
     * 修改用户关注
     * 
     * @param userFollows 用户关注
     * @return 结果
     */
    @Override
    public int updateUserFollows(UserFollows userFollows)
    {
        return userFollowsMapper.updateUserFollows(userFollows);
    }

    /**
     * 批量删除用户关注
     * 
     * @param ids 需要删除的用户关注主键
     * @return 结果
     */
    @Override
    public int deleteUserFollowsByIds(Long[] ids)
    {
        return userFollowsMapper.deleteUserFollowsByIds(ids);
    }

    /**
     * 删除用户关注信息
     * 
     * @param id 用户关注主键
     * @return 结果
     */
    @Override
    public int deleteUserFollowsById(Long id)
    {
        return userFollowsMapper.deleteUserFollowsById(id);
    }
}
