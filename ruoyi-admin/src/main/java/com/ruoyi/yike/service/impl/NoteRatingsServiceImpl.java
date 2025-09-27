package com.ruoyi.yike.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.yike.mapper.NoteRatingsMapper;
import com.ruoyi.yike.domain.NoteRatings;
import com.ruoyi.yike.service.INoteRatingsService;

/**
 * 笔记评分Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class NoteRatingsServiceImpl implements INoteRatingsService 
{
    @Autowired
    private NoteRatingsMapper noteRatingsMapper;

    /**
     * 查询笔记评分
     * 
     * @param id 笔记评分主键
     * @return 笔记评分
     */
    @Override
    public NoteRatings selectNoteRatingsById(Long id)
    {
        return noteRatingsMapper.selectNoteRatingsById(id);
    }

    /**
     * 查询笔记评分列表
     * 
     * @param noteRatings 笔记评分
     * @return 笔记评分
     */
    @Override
    public List<NoteRatings> selectNoteRatingsList(NoteRatings noteRatings)
    {
        return noteRatingsMapper.selectNoteRatingsList(noteRatings);
    }

    /**
     * 新增笔记评分
     * 
     * @param noteRatings 笔记评分
     * @return 结果
     */
    @Override
    public int insertNoteRatings(NoteRatings noteRatings)
    {
        return noteRatingsMapper.insertNoteRatings(noteRatings);
    }

    /**
     * 修改笔记评分
     * 
     * @param noteRatings 笔记评分
     * @return 结果
     */
    @Override
    public int updateNoteRatings(NoteRatings noteRatings)
    {
        return noteRatingsMapper.updateNoteRatings(noteRatings);
    }

    /**
     * 批量删除笔记评分
     * 
     * @param ids 需要删除的笔记评分主键
     * @return 结果
     */
    @Override
    public int deleteNoteRatingsByIds(Long[] ids)
    {
        return noteRatingsMapper.deleteNoteRatingsByIds(ids);
    }

    /**
     * 删除笔记评分信息
     * 
     * @param id 笔记评分主键
     * @return 结果
     */
    @Override
    public int deleteNoteRatingsById(Long id)
    {
        return noteRatingsMapper.deleteNoteRatingsById(id);
    }
}
