package com.ruoyi.yike.service;

import com.ruoyi.yike.domain.Note;
import java.util.List;

public interface NoteService {

    /**
     * 获取所有笔记（原有方法保持不变）
     */
    List<Note> getAllNotes();

    /**
     * 根据ID获取笔记详情（原有方法保持不变）
     */
    Note getNoteById(Long id);

    /**
     * 获取笔记简略信息列表（用于卡片显示）
     * 包含基本信息但不包含富文本内容
     */
    List<Note> getNoteCardsLatest(int page, int pageSize, Long subjectId, Long noteTypeId);

    List<Note> getNoteCardsMostRead(int page, int pageSize, Long subjectId, Long noteTypeId);

    List<Note> getNoteCardsTopRated(int page, int pageSize, Long subjectId, Long noteTypeId);

    List<Note> getNoteCardsMostFavorited(int page, int pageSize, Long subjectId, Long noteTypeId);


    /**
     * 根据ID获取笔记详细信息（包含完整内容）
     * 用于笔记详情页面显示
     */
    Note getNoteDetailById(Long id);

    int getNoteTotal(Long subjectId, Long noteTypeId);

    /**
     * 根据用户ID获取笔记卡片列表
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @param subjectId 学科ID（可选）
     * @param noteTypeId 笔记类型ID（可选）
     * @return 笔记卡片列表
     */
    List<Note> getNoteCardsByUserId(Long userId, int page, int pageSize, Long subjectId, Long noteTypeId);

    /**
     * 搜索笔记卡片列表
     * @param keyword 搜索关键词
     * @param subjectId 学科ID
     * @param noteTypeId 笔记类型ID
     * @param offset 偏移量
     * @param pageSize 页面大小
     * @return 笔记卡片列表
     */
    List<Note> getSearchNoteCards(String keyword, Long subjectId, Long noteTypeId, int offset, int pageSize);

    /**
     * 获取搜索结果总数 - 修复：添加缺失的方法
     * @param keyword 搜索关键词
     * @param subjectId 学科ID
     * @param noteTypeId 笔记类型ID
     * @return 搜索结果总数
     */
    int getSearchNoteTotal(String keyword, Long subjectId, Long noteTypeId);

    /**
     * 根据用户ID获取笔记总数
     * @param userId 用户ID
     * @param subjectId 学科ID（可选）
     * @param noteTypeId 笔记类型ID（可选）
     * @return 笔记总数
     */
    int getNoteCountByUserId(Long userId, Long subjectId, Long noteTypeId);

    boolean publishNote(Note note);
    boolean updateNote(Note note);

}