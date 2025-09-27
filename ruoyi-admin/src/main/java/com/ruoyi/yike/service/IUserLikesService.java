package com.ruoyi.yike.service;

import java.util.List;
import com.ruoyi.yike.domain.UserLikes;

/**
 * 用户点赞记录Service接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface IUserLikesService 
{
    /**
     * 查询用户点赞记录
     * 
     * @param id 用户点赞记录主键
     * @return 用户点赞记录
     */
    public UserLikes selectUserLikesById(Long id);

    /**
     * 查询用户点赞记录列表
     * 
     * @param userLikes 用户点赞记录
     * @return 用户点赞记录集合
     */
    public List<UserLikes> selectUserLikesList(UserLikes userLikes);

    /**
     * 新增用户点赞记录
     * 
     * @param userLikes 用户点赞记录
     * @return 结果
     */
    public int insertUserLikes(UserLikes userLikes);

    /**
     * 修改用户点赞记录
     * 
     * @param userLikes 用户点赞记录
     * @return 结果
     */
    public int updateUserLikes(UserLikes userLikes);

    /**
     * 批量删除用户点赞记录
     * 
     * @param ids 需要删除的用户点赞记录主键集合
     * @return 结果
     */
    public int deleteUserLikesByIds(Long[] ids);

    /**
     * 删除用户点赞记录信息
     * 
     * @param id 用户点赞记录主键
     * @return 结果
     */
    public int deleteUserLikesById(Long id);
}
