package com.ruoyi.yike.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.yike.mapper.NoteImagesMapper;
import com.ruoyi.yike.domain.NoteImages;
import com.ruoyi.yike.service.INoteImagesService;

/**
 * 笔记图片Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class NoteImagesServiceImpl implements INoteImagesService 
{
    @Autowired
    private NoteImagesMapper noteImagesMapper;

    /**
     * 查询笔记图片
     * 
     * @param id 笔记图片主键
     * @return 笔记图片
     */
    @Override
    public NoteImages selectNoteImagesById(Long id)
    {
        return noteImagesMapper.selectNoteImagesById(id);
    }

    /**
     * 查询笔记图片列表
     * 
     * @param noteImages 笔记图片
     * @return 笔记图片
     */
    @Override
    public List<NoteImages> selectNoteImagesList(NoteImages noteImages)
    {
        return noteImagesMapper.selectNoteImagesList(noteImages);
    }

    /**
     * 新增笔记图片
     * 
     * @param noteImages 笔记图片
     * @return 结果
     */
    @Override
    public int insertNoteImages(NoteImages noteImages)
    {
        return noteImagesMapper.insertNoteImages(noteImages);
    }

    /**
     * 修改笔记图片
     * 
     * @param noteImages 笔记图片
     * @return 结果
     */
    @Override
    public int updateNoteImages(NoteImages noteImages)
    {
        return noteImagesMapper.updateNoteImages(noteImages);
    }

    /**
     * 批量删除笔记图片
     * 
     * @param ids 需要删除的笔记图片主键
     * @return 结果
     */
    @Override
    public int deleteNoteImagesByIds(Long[] ids)
    {
        return noteImagesMapper.deleteNoteImagesByIds(ids);
    }

    /**
     * 删除笔记图片信息
     * 
     * @param id 笔记图片主键
     * @return 结果
     */
    @Override
    public int deleteNoteImagesById(Long id)
    {
        return noteImagesMapper.deleteNoteImagesById(id);
    }
}
