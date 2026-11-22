package com.moodlater.mapper;

import com.moodlater.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminFeedbackMapper {
    List<Feedback> selectAll();
    Feedback selectById(@Param("id") Long id);
}
