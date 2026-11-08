package com.moodlater.mapper;

import com.moodlater.entity.Banner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface BannerMapper {

    List<Banner> selectActive();

    List<Banner> selectAll();

    int insert(Banner banner);

    int updateById(Banner banner);

    int deleteById(@Param("id") Long id);
}
