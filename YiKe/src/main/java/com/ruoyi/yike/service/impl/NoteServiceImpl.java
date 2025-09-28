package com.ruoyi.yike.service.impl;

import com.ruoyi.yike.domain.Note;
import com.ruoyi.yike.mapper.NoteMapper;
import com.ruoyi.yike.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteMapper noteMapper;

    /**
     * 获取所有笔记（原有方法保持不变）
     */
    @Override
    public List<Note> getAllNotes() {
        return noteMapper.selectAllNotes();
    }

    /**
     * 根据ID获取笔记详情（原有方法保持不变）
     */
    @Override
    public Note getNoteById(Long id) {
        return noteMapper.selectNoteById(id);
    }

    /**
     * 获取最新发布的笔记卡片列表
     */
    @Override
    public List<Note> getNoteCardsLatest(int page, int pageSize, Long subjectId, Long noteTypeId) {
        int offset = (page - 1) * pageSize;
        return noteMapper.selectNoteCardsLatest(offset, pageSize, subjectId, noteTypeId);
    }

    /**
     * 获取最多阅读的笔记卡片列表
     */
    @Override
    public List<Note> getNoteCardsMostRead(int page, int pageSize, Long subjectId, Long noteTypeId) {
        int offset = (page - 1) * pageSize;
        return noteMapper.selectNoteCardsMostRead(offset, pageSize, subjectId, noteTypeId);
    }

    /**
     * 获取评分最高的笔记卡片列表
     */
    @Override
    public List<Note> getNoteCardsTopRated(int page, int pageSize, Long subjectId, Long noteTypeId) {
        int offset = (page - 1) * pageSize;
        return noteMapper.selectNoteCardsTopRated(offset, pageSize, subjectId, noteTypeId);
    }

    /**
     * 获取最多收藏的笔记卡片列表
     */
    @Override
    public List<Note> getNoteCardsMostFavorited(int page, int pageSize, Long subjectId, Long noteTypeId) {
        int offset = (page - 1) * pageSize;
        return noteMapper.selectNoteCardsMostFavorited(offset, pageSize, subjectId, noteTypeId);
    }

    /**
     * 根据ID获取笔记详细信息（包含完整内容）
     * 用于笔记详情页面显示，会自动增加浏览量
     */
    @Override
    public Note getNoteDetailById(Long id) {
        // 先增加浏览量
        noteMapper.incrementViews(id);
        // 再查询详情
        return noteMapper.selectNoteDetailById(id);
    }

    /**
     * 根据筛选条件获取笔记总数（修复：支持筛选条件）
     */
    @Override
    public int getNoteTotal(Long subjectId, Long noteTypeId) {
        return noteMapper.selectNoteTotal(subjectId, noteTypeId);
    }

    @Override
    public List<Note> getNoteCardsByUserId(Long userId, int page, int pageSize, Long subjectId, Long noteTypeId) {
        int offset = (page - 1) * pageSize;
        return noteMapper.selectNoteCardsByUserId(userId, offset, pageSize, subjectId, noteTypeId);
    }

    @Override
    public List<Note> getSearchNoteCards(String keyword, Long subjectId, Long noteTypeId, int offset, int pageSize) {
        return noteMapper.searchNoteCards(keyword, subjectId, noteTypeId, offset, pageSize);
    }

    /**
     * 获取搜索结果总数 - 修复：新增实现
     */
    @Override
    public int getSearchNoteTotal(String keyword, Long subjectId, Long noteTypeId) {
        return noteMapper.searchNoteTotal(keyword, subjectId, noteTypeId);
    }

    @Override
    public int getNoteCountByUserId(Long userId, Long subjectId, Long noteTypeId) {
        return noteMapper.selectNoteCountByUserId(userId, subjectId, noteTypeId);
    }

    @Override
    public boolean publishNote(Note note) {
        try {
            return noteMapper.insertNote(note) > 0;
        } catch (Exception e) {
            System.err.println("发布笔记失败：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateNote(Note note) {
        try {
            return noteMapper.updateNote(note) > 0;
        } catch (Exception e) {
            System.err.println("更新笔记失败：" + e.getMessage());
            return false;
        }
    }
}