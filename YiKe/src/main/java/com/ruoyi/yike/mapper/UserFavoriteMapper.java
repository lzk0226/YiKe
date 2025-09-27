package com.ruoyi.yike.mapper;

import com.ruoyi.yike.domain.UserFavorite;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户收藏Mapper接口
 *
 * @author ruoyi
 * @date 2025-09-26
 */
@Mapper
public interface UserFavoriteMapper
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
     * 删除用户收藏
     *
     * @param id 用户收藏主键
     * @return 结果
     */
    public int deleteUserFavoriteById(Long id);

    /**
     * 批量删除用户收藏
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserFavoriteByIds(Long[] ids);
}
