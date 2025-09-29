package com.ruoyi.yike.service.impl;

import com.ruoyi.yike.domain.UserFavorite;
import com.ruoyi.yike.mapper.UserFavoriteMapper;
import com.ruoyi.yike.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavoriteServiceImpl implements UserFavoriteService
{
    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    /**
     * 查询用户收藏
     *
     * @param id 用户收藏主键
     * @return 用户收藏
     */
    @Override
    public UserFavorite selectUserFavoriteById(Long id)
    {
        return userFavoriteMapper.selectUserFavoriteById(id);
    }

    /**
     * 查询用户收藏列表
     *
     * @param userFavorite 用户收藏
     * @return 用户收藏
     */
    @Override
    public List<UserFavorite> selectUserFavoriteList(UserFavorite userFavorite)
    {
        return userFavoriteMapper.selectUserFavoriteList(userFavorite);
    }

    /**
     * 新增用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 结果
     */
    @Override
    public int insertUserFavorite(UserFavorite userFavorite)
    {
        return userFavoriteMapper.insertUserFavorite(userFavorite);
    }

    /**
     * 修改用户收藏
     *
     * @param userFavorite 用户收藏
     * @return 结果
     */
    @Override
    public int updateUserFavorite(UserFavorite userFavorite)
    {
        return userFavoriteMapper.updateUserFavorite(userFavorite);
    }

    /**
     * 批量删除用户收藏
     *
     * @param ids 需要删除的用户收藏主键
     * @return 结果
     */
    @Override
    public int deleteUserFavoriteByIds(Long[] ids)
    {
        return userFavoriteMapper.deleteUserFavoriteByIds(ids);
    }

    /**
     * 删除用户收藏信息
     *
     * @param id 用户收藏主键
     * @return 结果
     */
    @Override
    public int deleteUserFavoriteById(Long id)
    {
        return userFavoriteMapper.deleteUserFavoriteById(id);
    }
}
