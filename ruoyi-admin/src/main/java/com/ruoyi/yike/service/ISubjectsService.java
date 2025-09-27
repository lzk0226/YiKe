package com.ruoyi.yike.service;

import java.util.List;
import com.ruoyi.yike.domain.Subjects;

/**
 * 学科分类Service接口
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
public interface ISubjectsService 
{
    /**
     * 查询学科分类
     * 
     * @param id 学科分类主键
     * @return 学科分类
     */
    public Subjects selectSubjectsById(Long id);

    /**
     * 查询学科分类列表
     * 
     * @param subjects 学科分类
     * @return 学科分类集合
     */
    public List<Subjects> selectSubjectsList(Subjects subjects);

    /**
     * 新增学科分类
     * 
     * @param subjects 学科分类
     * @return 结果
     */
    public int insertSubjects(Subjects subjects);

    /**
     * 修改学科分类
     * 
     * @param subjects 学科分类
     * @return 结果
     */
    public int updateSubjects(Subjects subjects);

    /**
     * 批量删除学科分类
     * 
     * @param ids 需要删除的学科分类主键集合
     * @return 结果
     */
    public int deleteSubjectsByIds(Long[] ids);

    /**
     * 删除学科分类信息
     * 
     * @param id 学科分类主键
     * @return 结果
     */
    public int deleteSubjectsById(Long id);
}
