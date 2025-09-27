package com.ruoyi.yike.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.yike.domain.NoteTypes;
import com.ruoyi.yike.service.INoteTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 笔记类型Controller
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@RestController
@RequestMapping("/yike/types")
public class NoteTypesController extends BaseController
{
    @Autowired
    private INoteTypesService noteTypesService;

    /**
     * 查询笔记类型列表
     */
    @PreAuthorize("@ss.hasPermi('yike:types:list')")
    @GetMapping("/list")
    public TableDataInfo list(NoteTypes noteTypes)
    {
        startPage();
        List<NoteTypes> list = noteTypesService.selectNoteTypesList(noteTypes);
        return getDataTable(list);
    }

    /**
     * 导出笔记类型列表
     */
    @PreAuthorize("@ss.hasPermi('yike:types:export')")
    @Log(title = "笔记类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NoteTypes noteTypes)
    {
        List<NoteTypes> list = noteTypesService.selectNoteTypesList(noteTypes);
        ExcelUtil<NoteTypes> util = new ExcelUtil<NoteTypes>(NoteTypes.class);
        util.exportExcel(response, list, "笔记类型数据");
    }

    /**
     * 获取笔记类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('yike:types:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(noteTypesService.selectNoteTypesById(id));
    }

    /**
     * 新增笔记类型
     */
    @PreAuthorize("@ss.hasPermi('yike:types:add')")
    @Log(title = "笔记类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NoteTypes noteTypes)
    {
        return toAjax(noteTypesService.insertNoteTypes(noteTypes));
    }

    /**
     * 修改笔记类型
     */
    @PreAuthorize("@ss.hasPermi('yike:types:edit')")
    @Log(title = "笔记类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NoteTypes noteTypes)
    {
        return toAjax(noteTypesService.updateNoteTypes(noteTypes));
    }

    /**
     * 删除笔记类型
     */
    @PreAuthorize("@ss.hasPermi('yike:types:remove')")
    @Log(title = "笔记类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(noteTypesService.deleteNoteTypesByIds(ids));
    }
}
