package com.ruoyi.yike.mapper;

import com.ruoyi.yike.domain.UserFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFollowMapper {
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
     * 删除用户关注
     *
     * @param id 用户关注主键
     * @return 结果
     */
    public int deleteUserFollowById(Long id);

    /**
     * 批量删除用户关注
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserFollowByIds(Long[] ids);

    /**
     * 检查关注关系是否存在
     *
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 结果
     */
    public int checkFollowExists(@Param("followerId") Long followerId,
                                 @Param("followingId") Long followingId);

    /**
     * 根据关注者和被关注者删除关注关系
     *
     * @param followerId 关注者ID
     * @param followingId 被关注者ID
     * @return 结果
     */
    public int deleteByFollowerAndFollowing(@Param("followerId") Long followerId,
                                            @Param("followingId") Long followingId);

    /**
     * 统计用户关注数
     *
     * @param userId 用户ID
     * @return 关注数
     */
    public int countFollowing(Long userId);

    /**
     * 统计用户粉丝数
     *
     * @param userId 用户ID
     * @return 粉丝数
     */
    public int countFollowers(Long userId);
}
