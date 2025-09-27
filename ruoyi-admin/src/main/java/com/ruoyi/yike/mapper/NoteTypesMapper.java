package com.ruoyi.yike.mapper;

import com.ruoyi.yike.domain.NoteTypes;

import java.util.List;

/**
 * 笔记类型Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface NoteTypesMapper 
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
     * 删除笔记类型
     * 
     * @param id 笔记类型主键
     * @return 结果
     */
    public int deleteNoteTypesById(Long id);

    /**
     * 批量删除笔记类型
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNoteTypesByIds(Long[] ids);
}
