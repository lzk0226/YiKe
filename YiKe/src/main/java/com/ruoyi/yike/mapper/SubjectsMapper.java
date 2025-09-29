package com.ruoyi.yike.mapper;

import com.ruoyi.yike.domain.Subject;

import java.util.List;


public interface SubjectsMapper 
{
    /**
     * 查询学科分类
     * 
     * @param id 学科分类主键
     * @return 学科分类
     */
    public Subject selectSubjectsById(Long id);

    /**
     * 查询学科分类列表
     * 
     * @param subjects 学科分类
     * @return 学科分类集合
     */
    public List<Subject> selectSubjectsList(Subject subjects);
}
