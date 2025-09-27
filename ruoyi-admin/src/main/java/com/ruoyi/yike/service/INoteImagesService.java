package com.ruoyi.yike.service;

import java.util.List;
import com.ruoyi.yike.domain.NoteImages;

/**
 * 笔记图片Service接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface INoteImagesService 
{
    /**
     * 查询笔记图片
     * 
     * @param id 笔记图片主键
     * @return 笔记图片
     */
    public NoteImages selectNoteImagesById(Long id);

    /**
     * 查询笔记图片列表
     * 
     * @param noteImages 笔记图片
     * @return 笔记图片集合
     */
    public List<NoteImages> selectNoteImagesList(NoteImages noteImages);

    /**
     * 新增笔记图片
     * 
     * @param noteImages 笔记图片
     * @return 结果
     */
    public int insertNoteImages(NoteImages noteImages);

    /**
     * 修改笔记图片
     * 
     * @param noteImages 笔记图片
     * @return 结果
     */
    public int updateNoteImages(NoteImages noteImages);

    /**
     * 批量删除笔记图片
     * 
     * @param ids 需要删除的笔记图片主键集合
     * @return 结果
     */
    public int deleteNoteImagesByIds(Long[] ids);

    /**
     * 删除笔记图片信息
     * 
     * @param id 笔记图片主键
     * @return 结果
     */
    public int deleteNoteImagesById(Long id);
}
