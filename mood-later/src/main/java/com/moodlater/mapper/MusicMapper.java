package com.moodlater.mapper;

import com.moodlater.entity.Music;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MusicMapper {

    List<Music> selectAll();

    Music selectById(@Param("id") Long id);

    int insert(Music music);

    int updateById(Music music);

    int deleteById(@Param("id") Long id);

    Music selectRandomByType(@Param("type") String type);

    Music selectRandomByTypeAndSinger(@Param("type") String type, @Param("singer") String singer);

    List<Music> selectByType(@Param("type") String type);

    List<String> selectDistinctSingers(@Param("type") String type);
}