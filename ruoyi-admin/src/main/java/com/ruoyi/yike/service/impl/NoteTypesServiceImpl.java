package com.ruoyi.yike.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.yike.mapper.NoteTypesMapper;
import com.ruoyi.yike.domain.NoteTypes;
import com.ruoyi.yike.service.INoteTypesService;

/**
 * 笔记类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class NoteTypesServiceImpl implements INoteTypesService 
{
    @Autowired
    private NoteTypesMapper noteTypesMapper;

    /**
     * 查询笔记类型
     * 
     * @param id 笔记类型主键
     * @return 笔记类型
     */
    @Override
    public NoteTypes selectNoteTypesById(Long id)
    {
        return noteTypesMapper.selectNoteTypesById(id);
    }

    /**
     * 查询笔记类型列表
     * 
     * @param noteTypes 笔记类型
     * @return 笔记类型
     */
    @Override
    public List<NoteTypes> selectNoteTypesList(NoteTypes noteTypes)
    {
        return noteTypesMapper.selectNoteTypesList(noteTypes);
    }

    /**
     * 新增笔记类型
     * 
     * @param noteTypes 笔记类型
     * @return 结果
     */
    @Override
    public int insertNoteTypes(NoteTypes noteTypes)
    {
        return noteTypesMapper.insertNoteTypes(noteTypes);
    }

    /**
     * 修改笔记类型
     * 
     * @param noteTypes 笔记类型
     * @return 结果
     */
    @Override
    public int updateNoteTypes(NoteTypes noteTypes)
    {
        return noteTypesMapper.updateNoteTypes(noteTypes);
    }

    /**
     * 批量删除笔记类型
     * 
     * @param ids 需要删除的笔记类型主键
     * @return 结果
     */
    @Override
    public int deleteNoteTypesByIds(Long[] ids)
    {
        return noteTypesMapper.deleteNoteTypesByIds(ids);
    }

    /**
     * 删除笔记类型信息
     * 
     * @param id 笔记类型主键
     * @return 结果
     */
    @Override
    public int deleteNoteTypesById(Long id)
    {
        return noteTypesMapper.deleteNoteTypesById(id);
    }
}
