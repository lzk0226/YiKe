package com.ruoyi.yike.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 热门笔记DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotNoteDTO {
    private Long id;
    private String title;
    private String description;
    private String subjectName;
    private String subjectCode;
    private String noteTypeName;
    private Long authorId;
    private String authorName;
    private String authorNickname;
    private String authorAvatar;      // 作者头像
    private Integer views;
    private Integer likes;
    private Integer favorites;
    private BigDecimal rating;
    private Integer ratingCount;
    private Double heatScore;  // 热度分数
    private LocalDateTime createdAt;
}