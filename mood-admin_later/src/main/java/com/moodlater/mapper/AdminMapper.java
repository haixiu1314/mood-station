package com.moodlater.mapper;

import com.moodlater.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {
    Admin selectByUsername(@Param("username") String username);
    Admin selectById(@Param("id") Long id);
}
