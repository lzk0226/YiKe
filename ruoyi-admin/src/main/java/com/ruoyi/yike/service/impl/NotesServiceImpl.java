package com.ruoyi.yike.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.yike.mapper.NotesMapper;
import com.ruoyi.yike.domain.Notes;
import com.ruoyi.yike.service.INotesService;

/**
 * 笔记Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class NotesServiceImpl implements INotesService 
{
    @Autowired
    private NotesMapper notesMapper;

    /**
     * 查询笔记
     * 
     * @param id 笔记主键
     * @return 笔记
     */
    @Override
    public Notes selectNotesById(Long id)
    {
        return notesMapper.selectNotesById(id);
    }

    /**
     * 查询笔记列表
     * 
     * @param notes 笔记
     * @return 笔记
     */
    @Override
    public List<Notes> selectNotesList(Notes notes)
    {
        return notesMapper.selectNotesList(notes);
    }

    /**
     * 新增笔记
     * 
     * @param notes 笔记
     * @return 结果
     */
    @Override
    public int insertNotes(Notes notes)
    {
        return notesMapper.insertNotes(notes);
    }

    /**
     * 修改笔记
     * 
     * @param notes 笔记
     * @return 结果
     */
    @Override
    public int updateNotes(Notes notes)
    {
        return notesMapper.updateNotes(notes);
    }

    /**
     * 批量删除笔记
     * 
     * @param ids 需要删除的笔记主键
     * @return 结果
     */
    @Override
    public int deleteNotesByIds(Long[] ids)
    {
        return notesMapper.deleteNotesByIds(ids);
    }

    /**
     * 删除笔记信息
     * 
     * @param id 笔记主键
     * @return 结果
     */
    @Override
    public int deleteNotesById(Long id)
    {
        return notesMapper.deleteNotesById(id);
    }
}
