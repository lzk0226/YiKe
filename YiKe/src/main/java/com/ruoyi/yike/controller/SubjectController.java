package com.ruoyi.yike.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.yike.domain.Subject;
import com.ruoyi.yike.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/subjects")
public class SubjectController extends BaseController
{
    @Autowired
    private SubjectService subjectService;

    /**
     * 查询学科分类列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Subject subjects)
    {
        startPage();
        List<Subject> list = subjectService.selectSubjectList(subjects);
        return getDataTable(list);
    }

    /**
     * 获取学科分类详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(subjectService.selectSubjectById(id));
    }
}
