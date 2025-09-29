package com.ruoyi.yike.service.impl;

import com.ruoyi.yike.domain.Subject;
import com.ruoyi.yike.mapper.SubjectMapper;
import com.ruoyi.yike.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService
{
    @Autowired
    private SubjectMapper subjectMapper;

    /**
     * 查询学科分类
     * 
     * @param id 学科分类主键
     * @return 学科分类
     */
    @Override
    public Subject selectSubjectById(Long id)
    {
        return subjectMapper.selectSubjectById(id);
    }

    /**
     * 查询学科分类列表
     * 
     * @param subjects 学科分类
     * @return 学科分类
     */
    @Override
    public List<Subject> selectSubjectList(Subject subjects)
    {
        return subjectMapper.selectSubjectList(subjects);
    }
}
