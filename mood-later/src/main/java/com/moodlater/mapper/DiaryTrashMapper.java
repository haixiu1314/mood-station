package com.moodlater.mapper;

import com.moodlater.entity.DiaryTrash;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DiaryTrashMapper {

    int insert(DiaryTrash diaryTrash);

    DiaryTrash selectById(@Param("id") Long id);

    List<DiaryTrash> selectByUserId(@Param("userId") Long userId);

    int deleteById(@Param("id") Long id, @Param("userId") Long userId);

    int deleteExpired();

    int countByUserId(@Param("userId") Long userId);
}
