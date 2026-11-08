package com.moodlater.mapper;

import com.moodlater.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NotificationMapper {
    int insert(Notification notification);
}
