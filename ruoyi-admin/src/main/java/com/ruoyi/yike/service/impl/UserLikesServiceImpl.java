package com.ruoyi.yike.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.yike.mapper.UserLikesMapper;
import com.ruoyi.yike.domain.UserLikes;
import com.ruoyi.yike.service.IUserLikesService;

/**
 * 用户点赞记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class UserLikesServiceImpl implements IUserLikesService 
{
    @Autowired
    private UserLikesMapper userLikesMapper;

    /**
     * 查询用户点赞记录
     * 
     * @param id 用户点赞记录主键
     * @return 用户点赞记录
     */
    @Override
    public UserLikes selectUserLikesById(Long id)
    {
        return userLikesMapper.selectUserLikesById(id);
    }

    /**
     * 查询用户点赞记录列表
     * 
     * @param userLikes 用户点赞记录
     * @return 用户点赞记录
     */
    @Override
    public List<UserLikes> selectUserLikesList(UserLikes userLikes)
    {
        return userLikesMapper.selectUserLikesList(userLikes);
    }

    /**
     * 新增用户点赞记录
     * 
     * @param userLikes 用户点赞记录
     * @return 结果
     */
    @Override
    public int insertUserLikes(UserLikes userLikes)
    {
        return userLikesMapper.insertUserLikes(userLikes);
    }

    /**
     * 修改用户点赞记录
     * 
     * @param userLikes 用户点赞记录
     * @return 结果
     */
    @Override
    public int updateUserLikes(UserLikes userLikes)
    {
        return userLikesMapper.updateUserLikes(userLikes);
    }

    /**
     * 批量删除用户点赞记录
     * 
     * @param ids 需要删除的用户点赞记录主键
     * @return 结果
     */
    @Override
    public int deleteUserLikesByIds(Long[] ids)
    {
        return userLikesMapper.deleteUserLikesByIds(ids);
    }

    /**
     * 删除用户点赞记录信息
     * 
     * @param id 用户点赞记录主键
     * @return 结果
     */
    @Override
    public int deleteUserLikesById(Long id)
    {
        return userLikesMapper.deleteUserLikesById(id);
    }
}
