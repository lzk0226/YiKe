package com.ruoyi.yike.service;

import java.util.List;
import com.ruoyi.yike.domain.NoteTypes;

/**
 * 笔记类型Service接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface INoteTypesService 
{
    /**
     * 查询笔记类型
     * 
     * @param id 笔记类型主键
     * @return 笔记类型
     */
    public NoteTypes selectNoteTypesById(Long id);

    /**
     * 查询笔记类型列表
     * 
     * @param noteTypes 笔记类型
     * @return 笔记类型集合
     */
    public List<NoteTypes> selectNoteTypesList(NoteTypes noteTypes);

    /**
     * 新增笔记类型
     * 
     * @param noteTypes 笔记类型
     * @return 结果
     */
    public int insertNoteTypes(NoteTypes noteTypes);

    /**
     * 修改笔记类型
     * 
     * @param noteTypes 笔记类型
     * @return 结果
     */
    public int updateNoteTypes(NoteTypes noteTypes);

    /**
     * 批量删除笔记类型
     * 
     * @param ids 需要删除的笔记类型主键集合
     * @return 结果
     */
    public int deleteNoteTypesByIds(Long[] ids);

    /**
     * 删除笔记类型信息
     * 
     * @param id 笔记类型主键
     * @return 结果
     */
    public int deleteNoteTypesById(Long id);
}
