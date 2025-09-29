package com.ruoyi.yike.mapper;

import com.ruoyi.yike.domain.Subject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubjectMapper
{
    /**
     * 查询学科分类
     * 
     * @param id 学科分类主键
     * @return 学科分类
     */
    public Subject selectSubjectById(Long id);

    /**
     * 查询学科分类列表
     * 
     * @param subjects 学科分类
     * @return 学科分类集合
     */
    public List<Subject> selectSubjectList(Subject subjects);
}
