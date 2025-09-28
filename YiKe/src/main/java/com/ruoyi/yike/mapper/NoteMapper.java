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

    // 在NoteMapper接口中新增以下方法：

    /**
     * 根据用户ID获取笔记卡片列表
     * @param userId 用户ID
     * @param offset 偏移量
     * @param pageSize 每页数量
     * @param subjectId 学科ID（可选）
     * @param noteTypeId 笔记类型ID（可选）
     * @return 笔记卡片列表
     */
    List<Note> selectNoteCardsByUserId(@Param("userId") Long userId,
                                       @Param("offset") int offset,
                                       @Param("pageSize") int pageSize,
                                       @Param("subjectId") Long subjectId,
                                       @Param("noteTypeId") Long noteTypeId);

    /**
     * 搜索笔记卡片列表（根据标题或作者模糊搜索）
     * @param keyword 搜索关键词
     * @param subjectId 学科ID（可选）
     * @param noteTypeId 笔记类型ID（可选）
     * @param offset 偏移量
     * @param pageSize 页面大小
     * @return 笔记卡片列表
     */
    List<Note> searchNoteCards(@Param("keyword") String keyword,
                               @Param("subjectId") Long subjectId,
                               @Param("noteTypeId") Long noteTypeId,
                               @Param("offset") int offset,
                               @Param("pageSize") int pageSize);

    /**
     * 获取搜索结果总数 - 修复：添加缺失的方法
     * @param keyword 搜索关键词
     * @param subjectId 学科ID（可选）
     * @param noteTypeId 笔记类型ID（可选）
     * @return 搜索结果总数
     */
    int searchNoteTotal(@Param("keyword") String keyword,
                        @Param("subjectId") Long subjectId,
                        @Param("noteTypeId") Long noteTypeId);

    /**
     * 根据用户ID获取笔记总数
     * @param userId 用户ID
     * @param subjectId 学科ID（可选）
     * @param noteTypeId 笔记类型ID（可选）
     * @return 笔记总数
     */
    int selectNoteCountByUserId(@Param("userId") Long userId,
                                @Param("subjectId") Long subjectId,
                                @Param("noteTypeId") Long noteTypeId);

    /**
     * 新增笔记
     * @param note 笔记信息
     * @return 影响行数
     */
    int insertNote(Note note);

    /**
     * 更新笔记信息
     * @param note 笔记信息
     * @return 影响行数
     */
    int updateNote(Note note);
}