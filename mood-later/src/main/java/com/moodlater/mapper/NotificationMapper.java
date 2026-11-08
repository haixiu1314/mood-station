package com.moodlater.mapper;

import com.moodlater.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface NotificationMapper {
    int insert(Notification notification);
    List<Notification> selectByUserId(@Param("userId") Long userId, @Param("category") String category);
    int countUnread(@Param("userId") Long userId);
    int countUnreadByCategory(@Param("userId") Long userId, @Param("category") String category);
    int markAsRead(@Param("id") Long id, @Param("userId") Long userId);
    int markAllAsRead(@Param("userId") Long userId);
    int markAllAsReadByCategory(@Param("userId") Long userId, @Param("category") String category);
}
