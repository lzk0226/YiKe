package com.ruoyi.yike.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 趋势笔记DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendingNoteDTO {
    private Long id;
    private String title;
    private String description;
    private String subjectName;
    private String subjectCode;
    private String noteTypeName;
    private Long authorId;
    private String authorName;
    private String authorNickname;
    private Integer views;
    private Integer likes;
    private Integer favorites;
    private BigDecimal rating;
    private Integer ratingCount;
    private Double heatScore;
    private String growthRate;        // 增长率
    private LocalDateTime createdAt;
}