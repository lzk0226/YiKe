package com.ruoyi.yike.mapper;

import java.util.List;
import com.ruoyi.yike.domain.Notes;

/**
 * 笔记Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface NotesMapper 
{
    /**
     * 查询笔记
     * 
     * @param id 笔记主键
     * @return 笔记
     */
    public Notes selectNotesById(Long id);

    /**
     * 查询笔记列表
     * 
     * @param notes 笔记
     * @return 笔记集合
     */
    public List<Notes> selectNotesList(Notes notes);

    /**
     * 新增笔记
     * 
     * @param notes 笔记
     * @return 结果
     */
    public int insertNotes(Notes notes);

    /**
     * 修改笔记
     * 
     * @param notes 笔记
     * @return 结果
     */
    public int updateNotes(Notes notes);

    /**
     * 删除笔记
     * 
     * @param id 笔记主键
     * @return 结果
     */
    public int deleteNotesById(Long id);

    /**
     * 批量删除笔记
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNotesByIds(Long[] ids);
}
