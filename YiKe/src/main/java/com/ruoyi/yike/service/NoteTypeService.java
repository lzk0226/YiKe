package com.ruoyi.yike.service;

import com.ruoyi.yike.domain.NoteType;

import java.util.List;

public interface NoteTypeService
{
    /**
     * 查询笔记类型
     * 
     * @param id 笔记类型主键
     * @return 笔记类型
     */
    public NoteType selectNoteTypeById(Long id);

    /**
     * 查询笔记类型列表
     * 
     * @param noteTypes 笔记类型
     * @return 笔记类型集合
     */
    public List<NoteType> selectNoteTypeList(NoteType noteTypes);
}
