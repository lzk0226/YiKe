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
import com.ruoyi.yike.domain.Subjects;
import com.ruoyi.yike.service.ISubjectsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 学科分类Controller
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@RestController
@RequestMapping("/yike/subjects")
public class SubjectsController extends BaseController
{
    @Autowired
    private ISubjectsService subjectsService;

    /**
     * 查询学科分类列表
     */
    @PreAuthorize("@ss.hasPermi('yike:subjects:list')")
    @GetMapping("/list")
    public TableDataInfo list(Subjects subjects)
    {
        startPage();
        List<Subjects> list = subjectsService.selectSubjectsList(subjects);
        return getDataTable(list);
    }

    /**
     * 导出学科分类列表
     */
    @PreAuthorize("@ss.hasPermi('yike:subjects:export')")
    @Log(title = "学科分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Subjects subjects)
    {
        List<Subjects> list = subjectsService.selectSubjectsList(subjects);
        ExcelUtil<Subjects> util = new ExcelUtil<Subjects>(Subjects.class);
        util.exportExcel(response, list, "学科分类数据");
    }

    /**
     * 获取学科分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('yike:subjects:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(subjectsService.selectSubjectsById(id));
    }

    /**
     * 新增学科分类
     */
    @PreAuthorize("@ss.hasPermi('yike:subjects:add')")
    @Log(title = "学科分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Subjects subjects)
    {
        return toAjax(subjectsService.insertSubjects(subjects));
    }

    /**
     * 修改学科分类
     */
    @PreAuthorize("@ss.hasPermi('yike:subjects:edit')")
    @Log(title = "学科分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Subjects subjects)
    {
        return toAjax(subjectsService.updateSubjects(subjects));
    }

    /**
     * 删除学科分类
     */
    @PreAuthorize("@ss.hasPermi('yike:subjects:remove')")
    @Log(title = "学科分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(subjectsService.deleteSubjectsByIds(ids));
    }
}
