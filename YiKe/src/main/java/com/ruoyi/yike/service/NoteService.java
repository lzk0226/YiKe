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
}