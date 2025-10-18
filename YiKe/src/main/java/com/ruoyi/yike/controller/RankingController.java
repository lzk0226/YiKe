package com.ruoyi.yike.controller;

import com.ruoyi.yike.DTO.RankingResponse;
import com.ruoyi.yike.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 排行榜控制器
 */
@RestController
@RequestMapping("/user/ranking")
@CrossOrigin(origins = "*")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    /**
     * 获取热门笔记排行榜
     * 按综合热度排序（浏览量、点赞数、收藏数、评分）
     */
    @GetMapping("/hot-notes")
    public ResponseEntity<RankingResponse> getHotNotes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(rankingService.getHotNotes(page, size));
    }

    /**
     * 获取优秀作者排行榜
     * 按作者的笔记总浏览量、总点赞数等排序
     */
    @GetMapping("/top-authors")
    public ResponseEntity<RankingResponse> getTopAuthors(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(rankingService.getTopAuthors(page, size));
    }

    /**
     * 获取本周趋势排行榜
     * 按近7天的数据增长趋势排序
     */
    @GetMapping("/trending")
    public ResponseEntity<RankingResponse> getTrending(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(rankingService.getTrending(page, size));
    }

    /**
     * 获取按学科分类的热门笔记
     */
    @GetMapping("/hot-notes/subject/{subjectId}")
    public ResponseEntity<RankingResponse> getHotNotesBySubject(
            @PathVariable int subjectId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(rankingService.getHotNotesBySubject(subjectId, page, size));
    }

    /**
     * 获取最新笔记排行
     */
    @GetMapping("/latest-notes")
    public ResponseEntity<RankingResponse> getLatestNotes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(rankingService.getLatestNotes(page, size));
    }

    /**
     * 获取评分最高的笔记
     */
    @GetMapping("/top-rated-notes")
    public ResponseEntity<RankingResponse> getTopRatedNotes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(rankingService.getTopRatedNotes(page, size));
    }
}