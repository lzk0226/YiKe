package com.ruoyi.yike.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.yike.mapper.UserFavoritesMapper;
import com.ruoyi.yike.domain.UserFavorites;
import com.ruoyi.yike.service.IUserFavoritesService;

/**
 * 用户收藏Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class UserFavoritesServiceImpl implements IUserFavoritesService 
{
    @Autowired
    private UserFavoritesMapper userFavoritesMapper;

    /**
     * 查询用户收藏
     * 
     * @param id 用户收藏主键
     * @return 用户收藏
     */
    @Override
    public UserFavorites selectUserFavoritesById(Long id)
    {
        return userFavoritesMapper.selectUserFavoritesById(id);
    }

    /**
     * 查询用户收藏列表
     * 
     * @param userFavorites 用户收藏
     * @return 用户收藏
     */
    @Override
    public List<UserFavorites> selectUserFavoritesList(UserFavorites userFavorites)
    {
        return userFavoritesMapper.selectUserFavoritesList(userFavorites);
    }

    /**
     * 新增用户收藏
     * 
     * @param userFavorites 用户收藏
     * @return 结果
     */
    @Override
    public int insertUserFavorites(UserFavorites userFavorites)
    {
        return userFavoritesMapper.insertUserFavorites(userFavorites);
    }

    /**
     * 修改用户收藏
     * 
     * @param userFavorites 用户收藏
     * @return 结果
     */
    @Override
    public int updateUserFavorites(UserFavorites userFavorites)
    {
        return userFavoritesMapper.updateUserFavorites(userFavorites);
    }

    /**
     * 批量删除用户收藏
     * 
     * @param ids 需要删除的用户收藏主键
     * @return 结果
     */
    @Override
    public int deleteUserFavoritesByIds(Long[] ids)
    {
        return userFavoritesMapper.deleteUserFavoritesByIds(ids);
    }

    /**
     * 删除用户收藏信息
     * 
     * @param id 用户收藏主键
     * @return 结果
     */
    @Override
    public int deleteUserFavoritesById(Long id)
    {
        return userFavoritesMapper.deleteUserFavoritesById(id);
    }
}
