package com.ruoyi.yike.service;

import com.ruoyi.yike.DTO.RankingResponse;

public interface RankingService {

    /**
     * 获取热门笔记排行榜
     */
    RankingResponse getHotNotes(int page, int size);

    /**
     * 获取优秀作者排行榜
     */
    RankingResponse getTopAuthors(int page, int size);

    /**
     * 获取本周趋势排行榜
     */
    RankingResponse getTrending(int page, int size);

    /**
     * 按学科获取热门笔记
     */
    RankingResponse getHotNotesBySubject(int subjectId, int page, int size);

    /**
     * 获取最新笔记
     */
    RankingResponse getLatestNotes(int page, int size);

    /**
     * 获取评分最高的笔记
     */
    RankingResponse getTopRatedNotes(int page, int size);
}