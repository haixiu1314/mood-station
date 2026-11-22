package com.moodlater.mapper;

import com.moodlater.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedbackMapper {

    int insert(Feedback feedback);
}
