package com.moodlater.mapper;

import com.moodlater.entity.Banner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminBannerMapper {
    List<Banner> selectAll();
    Banner selectById(@Param("id") Long id);
    int insert(Banner banner);
    int update(Banner banner);
    int deleteById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("isActive") Integer isActive);
}
