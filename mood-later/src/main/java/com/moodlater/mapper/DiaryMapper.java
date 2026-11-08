package com.moodlater.mapper;

import com.moodlater.entity.Diary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DiaryMapper {

    int insert(Diary diary);

    Diary selectById(@Param("id") Long id);

    List<Diary> selectByUserId(@Param("userId") Long userId);

    int updateById(Diary diary);

    int deleteById(@Param("id") Long id);

    List<Diary> selectRecentByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    int softDelete(@Param("id") Long id);

    int restore(@Param("id") Long id);

    List<Diary> selectByUserIdAndDate(@Param("userId") Long userId, @Param("date") String date);

    List<Diary> searchByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);

    List<Diary> selectPublicByUserId(@Param("userId") Long userId);
}
