package com.ruoyi.yike.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排行榜响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponse {
    private boolean success;
    private Object data;
    private int total;
    private int page;
    private int size;
    private String message;
}