package com.ruoyi.yike.mapper;

import com.ruoyi.yike.domain.NoteType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoteTypeMapper {
    NoteType selectNoteTypeById(Integer id);
    public List<NoteType> selectNoteTypeList(NoteType noteTypes);
}
