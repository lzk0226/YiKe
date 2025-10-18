package com.ruoyi.yike.service.impl;

import com.ruoyi.yike.DTO.HotNoteDTO;
import com.ruoyi.yike.DTO.RankingResponse;
import com.ruoyi.yike.DTO.TopAuthorDTO;
import com.ruoyi.yike.DTO.TrendingNoteDTO;
import com.ruoyi.yike.mapper.RankingMapper;
import com.ruoyi.yike.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * 文件类型/说明:
 * 文件创建时间:2025/10/18下午 9:23
 * @Author : SoakLightDust
 */
@Service
public class RankingServiceImpl implements RankingService {

    @Autowired
    private RankingMapper rankingMapper;

    @Override
    public RankingResponse getHotNotes(int page, int size) {
        int offset = (page - 1) * size;
        List<HotNoteDTO> notes = rankingMapper.getHotNotes(size, offset);
        int total = rankingMapper.countHotNotes();

        return RankingResponse.builder()
                .success(true)
                .data(notes)
                .total(total)
                .page(page)
                .size(size)
                .build();
    }

    @Override
    public RankingResponse getTopAuthors(int page, int size) {
        int offset = (page - 1) * size;
        List<TopAuthorDTO> authors = rankingMapper.getTopAuthors(size, offset);
        int total = rankingMapper.countTopAuthors();

        return RankingResponse.builder()
                .success(true)
                .data(authors)
                .total(total)
                .page(page)
                .size(size)
                .build();
    }

    @Override
    public RankingResponse getTrending(int page, int size) {
        int offset = (page - 1) * size;
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);

        List<TrendingNoteDTO> notes = rankingMapper.getTrendingNotes(weekAgo, size, offset);
        int total = rankingMapper.countTrendingNotes(weekAgo);

        return RankingResponse.builder()
                .success(true)
                .data(notes)
                .total(total)
                .page(page)
                .size(size)
                .build();
    }

    @Override
    public RankingResponse getHotNotesBySubject(int subjectId, int page, int size) {
        int offset = (page - 1) * size;
        List<HotNoteDTO> notes = rankingMapper.getHotNotesBySubject(subjectId, size, offset);
        int total = rankingMapper.countHotNotesBySubject(subjectId);

        return RankingResponse.builder()
                .success(true)
                .data(notes)
                .total(total)
                .page(page)
                .size(size)
                .build();
    }

    @Override
    public RankingResponse getLatestNotes(int page, int size) {
        int offset = (page - 1) * size;
        List<HotNoteDTO> notes = rankingMapper.getLatestNotes(size, offset);
        int total = rankingMapper.countLatestNotes();

        return RankingResponse.builder()
                .success(true)
                .data(notes)
                .total(total)
                .page(page)
                .size(size)
                .build();
    }

    @Override
    public RankingResponse getTopRatedNotes(int page, int size) {
        int offset = (page - 1) * size;
        List<HotNoteDTO> notes = rankingMapper.getTopRatedNotes(size, offset);
        int total = rankingMapper.countTopRatedNotes();

        return RankingResponse.builder()
                .success(true)
                .data(notes)
                .total(total)
                .page(page)
                .size(size)
                .build();
    }
}