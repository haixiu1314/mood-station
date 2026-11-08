package com.moodlater.mapper;

import com.moodlater.entity.Music;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminMusicMapper {

    List<Music> selectAll();

    Music selectById(@Param("id") Long id);

    int insert(Music music);

    int updateById(Music music);

    int deleteById(@Param("id") Long id);
}