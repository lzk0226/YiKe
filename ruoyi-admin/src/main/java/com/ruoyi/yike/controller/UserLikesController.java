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
import com.ruoyi.yike.domain.UserLikes;
import com.ruoyi.yike.service.IUserLikesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户点赞记录Controller
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@RestController
@RequestMapping("/yike/likes")
public class UserLikesController extends BaseController
{
    @Autowired
    private IUserLikesService userLikesService;

    /**
     * 查询用户点赞记录列表
     */
    @PreAuthorize("@ss.hasPermi('yike:likes:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserLikes userLikes)
    {
        startPage();
        List<UserLikes> list = userLikesService.selectUserLikesList(userLikes);
        return getDataTable(list);
    }

    /**
     * 导出用户点赞记录列表
     */
    @PreAuthorize("@ss.hasPermi('yike:likes:export')")
    @Log(title = "用户点赞记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserLikes userLikes)
    {
        List<UserLikes> list = userLikesService.selectUserLikesList(userLikes);
        ExcelUtil<UserLikes> util = new ExcelUtil<UserLikes>(UserLikes.class);
        util.exportExcel(response, list, "用户点赞记录数据");
    }

    /**
     * 获取用户点赞记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('yike:likes:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userLikesService.selectUserLikesById(id));
    }

    /**
     * 新增用户点赞记录
     */
    @PreAuthorize("@ss.hasPermi('yike:likes:add')")
    @Log(title = "用户点赞记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserLikes userLikes)
    {
        return toAjax(userLikesService.insertUserLikes(userLikes));
    }

    /**
     * 修改用户点赞记录
     */
    @PreAuthorize("@ss.hasPermi('yike:likes:edit')")
    @Log(title = "用户点赞记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserLikes userLikes)
    {
        return toAjax(userLikesService.updateUserLikes(userLikes));
    }

    /**
     * 删除用户点赞记录
     */
    @PreAuthorize("@ss.hasPermi('yike:likes:remove')")
    @Log(title = "用户点赞记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userLikesService.deleteUserLikesByIds(ids));
    }
}
