package com.ruoyi.yike.service.impl;

import com.ruoyi.yike.domain.NoteType;
import com.ruoyi.yike.mapper.NoteTypeMapper;
import com.ruoyi.yike.service.NoteTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 笔记类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class NoteTypeServiceImpl implements NoteTypeService
{
    @Autowired
    private NoteTypeMapper noteTypeMapper;

    /**
     * 查询笔记类型
     * 
     * @param id 笔记类型主键
     * @return 笔记类型
     */
    @Override
    public NoteType selectNoteTypeById(Long id)
    {
        return noteTypeMapper.selectNoteTypeById(Math.toIntExact(id));
    }

    /**
     * 查询笔记类型列表
     * 
     * @param noteTypes 笔记类型
     * @return 笔记类型
     */
    @Override
    public List<NoteType> selectNoteTypeList(NoteType noteTypes)
    {
        return noteTypeMapper.selectNoteTypeList(noteTypes);
    }

}
