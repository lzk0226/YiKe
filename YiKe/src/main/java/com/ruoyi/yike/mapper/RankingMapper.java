package com.ruoyi.yike.mapper;

import com.ruoyi.yike.DTO.HotNoteDTO;
import com.ruoyi.yike.DTO.TopAuthorDTO;
import com.ruoyi.yike.DTO.TrendingNoteDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 排行榜Mapper（XML方式）
 */
@Mapper
public interface RankingMapper {

    // 热门笔记
    List<HotNoteDTO> getHotNotes(@Param("limit") int limit, @Param("offset") int offset);
    int countHotNotes();

    // 优秀作者
    List<TopAuthorDTO> getTopAuthors(@Param("limit") int limit, @Param("offset") int offset);
    int countTopAuthors();

    // 本周趋势笔记
    List<TrendingNoteDTO> getTrendingNotes(@Param("weekAgo") LocalDateTime weekAgo,
                                           @Param("limit") int limit,
                                           @Param("offset") int offset);
    int countTrendingNotes(@Param("weekAgo") LocalDateTime weekAgo);

    // 按学科热门笔记
    List<HotNoteDTO> getHotNotesBySubject(@Param("subjectId") int subjectId,
                                          @Param("limit") int limit,
                                          @Param("offset") int offset);
    int countHotNotesBySubject(@Param("subjectId") int subjectId);

    // 最新笔记
    List<HotNoteDTO> getLatestNotes(@Param("limit") int limit, @Param("offset") int offset);
    int countLatestNotes();

    // 评分最高笔记
    List<HotNoteDTO> getTopRatedNotes(@Param("limit") int limit, @Param("offset") int offset);
    int countTopRatedNotes();
}
