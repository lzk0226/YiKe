package com.ruoyi.yike.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.yike.domain.Notes;
import com.ruoyi.yike.service.INotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 笔记Controller
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@RestController
@RequestMapping("/yike/notes")
public class NotesController extends BaseController
{
    @Autowired
    private INotesService notesService;

    /**
     * 查询笔记列表
     */
    @PreAuthorize("@ss.hasPermi('yike:notes:list')")
    @GetMapping("/list")
    public TableDataInfo list(Notes notes)
    {
        startPage();
        List<Notes> list = notesService.selectNotesList(notes);
        return getDataTable(list);
    }

    /**
     * 导出笔记列表
     */
    @PreAuthorize("@ss.hasPermi('yike:notes:export')")
    @Log(title = "笔记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Notes notes)
    {
        List<Notes> list = notesService.selectNotesList(notes);
        ExcelUtil<Notes> util = new ExcelUtil<Notes>(Notes.class);
        util.exportExcel(response, list, "笔记数据");
    }

    /**
     * 获取笔记详细信息
     */
    @PreAuthorize("@ss.hasPermi('yike:notes:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(notesService.selectNotesById(id));
    }

    /**
     * 新增笔记
     */
    @PreAuthorize("@ss.hasPermi('yike:notes:add')")
    @Log(title = "笔记", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Notes notes)
    {
        return toAjax(notesService.insertNotes(notes));
    }

    /**
     * 修改笔记
     */
    @PreAuthorize("@ss.hasPermi('yike:notes:edit')")
    @Log(title = "笔记", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Notes notes)
    {
        return toAjax(notesService.updateNotes(notes));
    }

    /**
     * 删除笔记
     */
    @PreAuthorize("@ss.hasPermi('yike:notes:remove')")
    @Log(title = "笔记", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(notesService.deleteNotesByIds(ids));
    }
}
