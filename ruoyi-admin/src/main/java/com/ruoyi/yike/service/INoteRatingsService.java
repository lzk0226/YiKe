package com.ruoyi.yike.service;

import java.util.List;
import com.ruoyi.yike.domain.NoteRatings;

/**
 * 笔记评分Service接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface INoteRatingsService 
{
    /**
     * 查询笔记评分
     * 
     * @param id 笔记评分主键
     * @return 笔记评分
     */
    public NoteRatings selectNoteRatingsById(Long id);

    /**
     * 查询笔记评分列表
     * 
     * @param noteRatings 笔记评分
     * @return 笔记评分集合
     */
    public List<NoteRatings> selectNoteRatingsList(NoteRatings noteRatings);

    /**
     * 新增笔记评分
     * 
     * @param noteRatings 笔记评分
     * @return 结果
     */
    public int insertNoteRatings(NoteRatings noteRatings);

    /**
     * 修改笔记评分
     * 
     * @param noteRatings 笔记评分
     * @return 结果
     */
    public int updateNoteRatings(NoteRatings noteRatings);

    /**
     * 批量删除笔记评分
     * 
     * @param ids 需要删除的笔记评分主键集合
     * @return 结果
     */
    public int deleteNoteRatingsByIds(Long[] ids);

    /**
     * 删除笔记评分信息
     * 
     * @param id 笔记评分主键
     * @return 结果
     */
    public int deleteNoteRatingsById(Long id);
}
