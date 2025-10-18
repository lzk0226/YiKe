package com.ruoyi.yike.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 优秀作者DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopAuthorDTO {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String school;
    private String major;
    private Integer noteCount;        // 笔记数量
    private Long totalViews;          // 总浏览量
    private Long totalLikes;          // 总点赞数
    private Long totalFavorites;      // 总收藏数
    private Integer favoriteCount;    // 收藏笔记数
    private Integer commentCount;     // 评论数
    private Double authorScore;       // 作者分数
}