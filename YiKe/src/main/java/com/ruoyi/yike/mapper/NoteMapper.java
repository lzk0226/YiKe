package com.ruoyi.yike.mapper;

import com.ruoyi.yike.domain.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NoteMapper {

    /**
     * 查询所有笔记（原有方法）
     */
    List<Note> selectAllNotes();

    /**
     * 根据ID查询笔记（原有方法）
     */
    Note selectNoteById(Long id);

    /**
     * 查询笔记卡片列表（简略版本）
     * 不包含content字段，减少数据传输量
     */
    List<Note> selectNoteCardsLatest(@Param("offset") int offset,
                                     @Param("pageSize") int pageSize,
                                     @Param("subjectId") Long subjectId,
                                     @Param("noteTypeId") Long noteTypeId);

    List<Note> selectNoteCardsMostRead(@Param("offset") int offset,
                                       @Param("pageSize") int pageSize,
                                       @Param("subjectId") Long subjectId,
                                       @Param("noteTypeId") Long noteTypeId);

    List<Note> selectNoteCardsTopRated(@Param("offset") int offset,
                                       @Param("pageSize") int pageSize,
                                       @Param("subjectId") Long subjectId,
                                       @Param("noteTypeId") Long noteTypeId);

    List<Note> selectNoteCardsMostFavorited(@Param("offset") int offset,
                                            @Param("pageSize") int pageSize,
                                            @Param("subjectId") Long subjectId,
                                            @Param("noteTypeId") Long noteTypeId);

    /**
     * 根据ID查询笔记详情（完整版本）
     * 包含所有字段和关联信息
     */
    Note selectNoteDetailById(Long id);

    /**
     * 增加浏览量
     */
    @Update("UPDATE notes SET views = views + 1 WHERE id = #{id}")
    void incrementViews(Long id);

    /**
     * 根据条件查询笔记总数（修复：支持筛选条件）
     */
    int selectNoteTotal(@Param("subjectId") Long subjectId,
                        @Param("noteTypeId") Long noteTypeId);
}