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
import com.ruoyi.yike.domain.NoteRatings;
import com.ruoyi.yike.service.INoteRatingsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 笔记评分Controller
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@RestController
@RequestMapping("/yike/ratings")
public class NoteRatingsController extends BaseController
{
    @Autowired
    private INoteRatingsService noteRatingsService;

    /**
     * 查询笔记评分列表
     */
    @PreAuthorize("@ss.hasPermi('yike:ratings:list')")
    @GetMapping("/list")
    public TableDataInfo list(NoteRatings noteRatings)
    {
        startPage();
        List<NoteRatings> list = noteRatingsService.selectNoteRatingsList(noteRatings);
        return getDataTable(list);
    }

    /**
     * 导出笔记评分列表
     */
    @PreAuthorize("@ss.hasPermi('yike:ratings:export')")
    @Log(title = "笔记评分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NoteRatings noteRatings)
    {
        List<NoteRatings> list = noteRatingsService.selectNoteRatingsList(noteRatings);
        ExcelUtil<NoteRatings> util = new ExcelUtil<NoteRatings>(NoteRatings.class);
        util.exportExcel(response, list, "笔记评分数据");
    }

    /**
     * 获取笔记评分详细信息
     */
    @PreAuthorize("@ss.hasPermi('yike:ratings:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(noteRatingsService.selectNoteRatingsById(id));
    }

    /**
     * 新增笔记评分
     */
    @PreAuthorize("@ss.hasPermi('yike:ratings:add')")
    @Log(title = "笔记评分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NoteRatings noteRatings)
    {
        return toAjax(noteRatingsService.insertNoteRatings(noteRatings));
    }

    /**
     * 修改笔记评分
     */
    @PreAuthorize("@ss.hasPermi('yike:ratings:edit')")
    @Log(title = "笔记评分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NoteRatings noteRatings)
    {
        return toAjax(noteRatingsService.updateNoteRatings(noteRatings));
    }

    /**
     * 删除笔记评分
     */
    @PreAuthorize("@ss.hasPermi('yike:ratings:remove')")
    @Log(title = "笔记评分", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(noteRatingsService.deleteNoteRatingsByIds(ids));
    }
}
