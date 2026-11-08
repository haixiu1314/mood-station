package com.moodlater.mapper;

import com.moodlater.entity.UserMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMessageMapper {

    int insert(UserMessage message);

    List<UserMessage> selectByToUserId(@Param("toUserId") Long toUserId);

    List<UserMessage> selectByFromUserId(@Param("fromUserId") Long fromUserId);

    int countUnread(@Param("toUserId") Long toUserId);

    int markAsRead(@Param("id") Long id, @Param("toUserId") Long toUserId);

    int markAllAsRead(@Param("toUserId") Long toUserId);
}
