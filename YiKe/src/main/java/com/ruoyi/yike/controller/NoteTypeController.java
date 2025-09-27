package com.ruoyi.yike.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.yike.domain.NoteType;
import com.ruoyi.yike.service.NoteTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 笔记类型Controller
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@RestController
@RequestMapping("/user/types")
public class NoteTypeController extends BaseController
{
    @Autowired
    private NoteTypeService noteTypeService;

    /**
     * 查询笔记类型列表
     */
    @GetMapping("/list")
    public TableDataInfo list(NoteType noteTypes)
    {
        startPage();
        List<NoteType> list = noteTypeService.selectNoteTypeList(noteTypes);
        return getDataTable(list);
    }
}
