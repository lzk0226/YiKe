package com.ruoyi.yike.service.impl;

import com.ruoyi.yike.domain.Note;
import com.ruoyi.yike.mapper.NoteMapper;
import com.ruoyi.yike.service.NoteService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public List<Note> getAllNotes() {
        return noteMapper.selectAllNotes();
    }

    @Override
    public Note getNoteById(Long id) {
        return noteMapper.selectNoteById(id);
    }

    private int calculateOffset(int page, int pageSize) {
        return (page - 1) * pageSize;
    }

    @Override
    public List<Note> getNoteCardsLatest(int page, int pageSize, Long subjectId, Long noteTypeId) {
        int offset = calculateOffset(page, pageSize);
        return noteMapper.selectNoteCardsLatest(offset, pageSize, subjectId, noteTypeId);
    }

    @Override
    public List<Note> getNoteCardsMostRead(int page, int pageSize, Long subjectId, Long noteTypeId) {
        int offset = calculateOffset(page, pageSize);
        return noteMapper.selectNoteCardsMostRead(offset, pageSize, subjectId, noteTypeId);
    }

    @Override
    public List<Note> getNoteCardsTopRated(int page, int pageSize, Long subjectId, Long noteTypeId) {
        int offset = calculateOffset(page, pageSize);
        return noteMapper.selectNoteCardsTopRated(offset, pageSize, subjectId, noteTypeId);
    }

    @Override
    public List<Note> getNoteCardsMostFavorited(int page, int pageSize, Long subjectId, Long noteTypeId) {
        int offset = calculateOffset(page, pageSize);
        return noteMapper.selectNoteCardsMostFavorited(offset, pageSize, subjectId, noteTypeId);
    }


    @Override
    public Note getNoteDetailById(Long id) {
        // 这里可以增加浏览量
        noteMapper.incrementViews(id);
        return noteMapper.selectNoteDetailById(id);
    }

    @Override
    public int getNoteTotal(Long subjectId, Long noteTypeId) {
        return noteMapper.selectNoteTotal(subjectId, noteTypeId);
    }
}