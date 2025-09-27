package com.ruoyi.yike.service;

import java.util.List;
import com.ruoyi.yike.domain.Notes;

/**
 * 笔记Service接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface INotesService 
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
     * 批量删除笔记
     * 
     * @param ids 需要删除的笔记主键集合
     * @return 结果
     */
    public int deleteNotesByIds(Long[] ids);

    /**
     * 删除笔记信息
     * 
     * @param id 笔记主键
     * @return 结果
     */
    public int deleteNotesById(Long id);
}
