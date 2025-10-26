package com.ruoyi.yike.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.yike.domain.UserFollows;
import com.ruoyi.yike.service.IUserFollowsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户关注Controller
 * 
 * @author ruoyi
 * @date 2025-10-26
 */
@RestController
@RequestMapping("/yike/follows")
public class UserFollowsController extends BaseController
{
    @Autowired
    private IUserFollowsService userFollowsService;

    /**
     * 查询用户关注列表
     */
    @PreAuthorize("@ss.hasPermi('yike:follows:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserFollows userFollows)
    {
        startPage();
        List<UserFollows> list = userFollowsService.selectUserFollowsList(userFollows);
        return getDataTable(list);
    }

    /**
     * 导出用户关注列表
     */
    @PreAuthorize("@ss.hasPermi('yike:follows:export')")
    @Log(title = "用户关注", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserFollows userFollows)
    {
        List<UserFollows> list = userFollowsService.selectUserFollowsList(userFollows);
        ExcelUtil<UserFollows> util = new ExcelUtil<UserFollows>(UserFollows.class);
        util.exportExcel(response, list, "用户关注数据");
    }

    /**
     * 获取用户关注详细信息
     */
    @PreAuthorize("@ss.hasPermi('yike:follows:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userFollowsService.selectUserFollowsById(id));
    }

    /**
     * 新增用户关注
     */
    @PreAuthorize("@ss.hasPermi('yike:follows:add')")
    @Log(title = "用户关注", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserFollows userFollows)
    {
        return toAjax(userFollowsService.insertUserFollows(userFollows));
    }

    /**
     * 修改用户关注
     */
    @PreAuthorize("@ss.hasPermi('yike:follows:edit')")
    @Log(title = "用户关注", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserFollows userFollows)
    {
        return toAjax(userFollowsService.updateUserFollows(userFollows));
    }

    /**
     * 删除用户关注
     */
    @PreAuthorize("@ss.hasPermi('yike:follows:remove')")
    @Log(title = "用户关注", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userFollowsService.deleteUserFollowsByIds(ids));
    }
}
