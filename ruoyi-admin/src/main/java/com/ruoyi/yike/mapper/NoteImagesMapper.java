package com.ruoyi.yike.mapper;

import java.util.List;
import com.ruoyi.yike.domain.NoteImages;

/**
 * 笔记图片Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface NoteImagesMapper 
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
     * 删除笔记图片
     * 
     * @param id 笔记图片主键
     * @return 结果
     */
    public int deleteNoteImagesById(Long id);

    /**
     * 批量删除笔记图片
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNoteImagesByIds(Long[] ids);
}
