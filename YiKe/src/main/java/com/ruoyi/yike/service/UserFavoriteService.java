package com.ruoyi.yike.service;

import com.ruoyi.yike.domain.UserFavorite;

import java.util.List;

public interface UserFavoriteService
{
    /**
     * 查询用户收藏
     *
     * @param id 用户收藏主键
     * @return 用户收藏
     */
    public UserFavorite selectUserFavoriteById(Long id);

    /**
     * 查询用户收藏列表
     *
     * @param userFavorite 用户收藏
     * @return 用户收藏集合
     */
    public List<UserFavorite> selectUserFavoriteList(UserFavorite userFavorite);

    /**
     * 新增用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 结果
     */
    public int insertUserFavorite(UserFavorite userFavorite);

    /**
     * 修改用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 结果
     */
    public int updateUserFavorite(UserFavorite userFavorite);

    /**
     * 批量删除用户收藏
     *
     * @param ids 需要删除的用户收藏主键集合
     * @return 结果
     */
    public int deleteUserFavoriteByIds(Long[] ids);

    /**
     * 删除用户收藏信息
     *
     * @param id 用户收藏主键
     * @return 结果
     */
    public int deleteUserFavoriteById(Long id);
}
