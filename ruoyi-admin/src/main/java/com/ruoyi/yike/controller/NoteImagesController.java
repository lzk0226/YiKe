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
import com.ruoyi.yike.domain.NoteImages;
import com.ruoyi.yike.service.INoteImagesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 笔记图片Controller
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@RestController
@RequestMapping("/yike/images")
public class NoteImagesController extends BaseController
{
    @Autowired
    private INoteImagesService noteImagesService;

    /**
     * 查询笔记图片列表
     */
    @PreAuthorize("@ss.hasPermi('yike:images:list')")
    @GetMapping("/list")
    public TableDataInfo list(NoteImages noteImages)
    {
        startPage();
        List<NoteImages> list = noteImagesService.selectNoteImagesList(noteImages);
        return getDataTable(list);
    }

    /**
     * 导出笔记图片列表
     */
    @PreAuthorize("@ss.hasPermi('yike:images:export')")
    @Log(title = "笔记图片", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NoteImages noteImages)
    {
        List<NoteImages> list = noteImagesService.selectNoteImagesList(noteImages);
        ExcelUtil<NoteImages> util = new ExcelUtil<NoteImages>(NoteImages.class);
        util.exportExcel(response, list, "笔记图片数据");
    }

    /**
     * 获取笔记图片详细信息
     */
    @PreAuthorize("@ss.hasPermi('yike:images:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(noteImagesService.selectNoteImagesById(id));
    }

    /**
     * 新增笔记图片
     */
    @PreAuthorize("@ss.hasPermi('yike:images:add')")
    @Log(title = "笔记图片", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NoteImages noteImages)
    {
        return toAjax(noteImagesService.insertNoteImages(noteImages));
    }

    /**
     * 修改笔记图片
     */
    @PreAuthorize("@ss.hasPermi('yike:images:edit')")
    @Log(title = "笔记图片", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NoteImages noteImages)
    {
        return toAjax(noteImagesService.updateNoteImages(noteImages));
    }

    /**
     * 删除笔记图片
     */
    @PreAuthorize("@ss.hasPermi('yike:images:remove')")
    @Log(title = "笔记图片", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(noteImagesService.deleteNoteImagesByIds(ids));
    }
}
