package com.ruoyi.yike.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.yike.mapper.SubjectsMapper;
import com.ruoyi.yike.domain.Subjects;
import com.ruoyi.yike.service.ISubjectsService;

/**
 * 学科分类Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-26
 */
@Service
public class SubjectsServiceImpl implements ISubjectsService 
{
    @Autowired
    private SubjectsMapper subjectsMapper;

    /**
     * 查询学科分类
     * 
     * @param id 学科分类主键
     * @return 学科分类
     */
    @Override
    public Subjects selectSubjectsById(Long id)
    {
        return subjectsMapper.selectSubjectsById(id);
    }

    /**
     * 查询学科分类列表
     * 
     * @param subjects 学科分类
     * @return 学科分类
     */
    @Override
    public List<Subjects> selectSubjectsList(Subjects subjects)
    {
        return subjectsMapper.selectSubjectsList(subjects);
    }

    /**
     * 新增学科分类
     * 
     * @param subjects 学科分类
     * @return 结果
     */
    @Override
    public int insertSubjects(Subjects subjects)
    {
        return subjectsMapper.insertSubjects(subjects);
    }

    /**
     * 修改学科分类
     * 
     * @param subjects 学科分类
     * @return 结果
     */
    @Override
    public int updateSubjects(Subjects subjects)
    {
        return subjectsMapper.updateSubjects(subjects);
    }

    /**
     * 批量删除学科分类
     * 
     * @param ids 需要删除的学科分类主键
     * @return 结果
     */
    @Override
    public int deleteSubjectsByIds(Long[] ids)
    {
        return subjectsMapper.deleteSubjectsByIds(ids);
    }

    /**
     * 删除学科分类信息
     * 
     * @param id 学科分类主键
     * @return 结果
     */
    @Override
    public int deleteSubjectsById(Long id)
    {
        return subjectsMapper.deleteSubjectsById(id);
    }
}
