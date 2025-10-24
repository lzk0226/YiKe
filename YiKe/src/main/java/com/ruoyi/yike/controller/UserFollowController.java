package com.ruoyi.yike.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.yike.domain.UserFollow;
import com.ruoyi.yike.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/10/23下午 6:25
 * @Author : SoakLightDust
 */
@RestController
@RequestMapping("/user/follow")
public class UserFollowController extends BaseController {
    @Autowired
    private UserFollowService userFollowService;

    /**
     * 查询所有用户关注列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserFollow userFollow) {
        startPage();
        List<UserFollow> list = userFollowService.selectUserFollowList(userFollow);
        return getDataTable(list);
    }

    /**
     * 获取单个用户关注详情
     */
    @GetMapping
    public AjaxResult getInfo(@RequestParam("id") Long id) {
        return success(userFollowService.selectUserFollowById(id));
    }

    /**
     * 新增用户关注
     */
    @PostMapping
    public AjaxResult add(@RequestBody UserFollow userFollow) {
        return toAjax(userFollowService.insertUserFollow(userFollow));
    }

    /**
     * 修改用户关注
     */
    @PutMapping
    public AjaxResult edit(@RequestBody UserFollow userFollow) {
        return toAjax(userFollowService.updateUserFollow(userFollow));
    }

    /**
     * 删除用户关注
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userFollowService.deleteUserFollowByIds(ids));
    }

    /**
     * 关注用户
     */
    @PostMapping("/follow")
    public AjaxResult follow(@RequestParam("followerId") Long followerId,
                             @RequestParam("followingId") Long followingId) {
        if (followerId.equals(followingId)) {
            return error("不能关注自己");
        }
        int result = userFollowService.followUser(followerId, followingId);
        if (result > 0) {
            return success("关注成功");
        } else {
            return error("已经关注过该用户");
        }
    }

    /**
     * 取消关注
     */
    @DeleteMapping("/unfollow")
    public AjaxResult unfollow(@RequestParam("followerId") Long followerId,
                               @RequestParam("followingId") Long followingId) {
        int result = userFollowService.unfollowUser(followerId, followingId);
        if (result > 0) {
            return success("取消关注成功");
        } else {
            return error("未关注该用户");
        }
    }

    /**
     * 检查是否已关注
     */
    @GetMapping("/isFollowing")
    public AjaxResult isFollowing(@RequestParam("followerId") Long followerId,
                                  @RequestParam("followingId") Long followingId) {
        boolean following = userFollowService.isFollowing(followerId, followingId);
        return success(following);
    }

    /**
     * 获取用户关注数
     */
    @GetMapping("/followingCount")
    public AjaxResult getFollowingCount(@RequestParam("userId") Long userId) {
        int count = userFollowService.getFollowingCount(userId);
        return success(count);
    }

    /**
     * 获取用户粉丝数
     */
    @GetMapping("/followerCount")
    public AjaxResult getFollowerCount(@RequestParam("userId") Long userId) {
        int count = userFollowService.getFollowerCount(userId);
        return success(count);
    }
}