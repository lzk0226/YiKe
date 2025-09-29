package com.ruoyi.yike.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.yike.domain.UserFavorite;
import com.ruoyi.yike.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/Favorite")
public class UserFavoriteController extends BaseController
{
    @Autowired
    private UserFavoriteService userFavoriteService;

    /**
     * 查询所有用户收藏列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserFavorite userFavorite)
    {
        startPage();
        List<UserFavorite> list = userFavoriteService.selectUserFavoriteList(userFavorite);
        return getDataTable(list);
    }

    /**
     * 获取单个用户收藏列表
     */
    @GetMapping
    public AjaxResult getInfo(@RequestParam("id") Long id) {
        return success(userFavoriteService.selectUserFavoriteById(id));
    }


    /**
     * 新增用户收藏
     */
    @PostMapping
    public AjaxResult add(@RequestBody UserFavorite userFavorite)
    {
        return toAjax(userFavoriteService.insertUserFavorite(userFavorite));
    }

    /**
     * 修改用户收藏
     */
    @PutMapping
    public AjaxResult edit(@RequestBody UserFavorite userFavorite)
    {
        return toAjax(userFavoriteService.updateUserFavorite(userFavorite));
    }

    /**
     * 删除用户收藏
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userFavoriteService.deleteUserFavoriteByIds(ids));
    }
}
