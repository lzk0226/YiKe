package com.ruoyi.yike.mapper;

import com.ruoyi.yike.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface CommentMapper {

    /** 查询所有评论 */
    List<Comment> selectAllComments();

    /** 根据笔记ID查询评论列表 */
    List<Comment> selectCommentsByNoteId(@Param("noteId") Long noteId);

    /** 根据评论ID查询单条评论 */
    Comment selectCommentById(@Param("id") Long id);

    /** 新增评论 */
    int insertComment(Comment comment);

    /** 更新评论点赞数 */
    int updateCommentLikes(@Param("id") Long id, @Param("likes") Integer likes);
}
