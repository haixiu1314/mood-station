package com.moodlater.mapper;

import com.moodlater.entity.AiChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiChatMapper {

    /**
     * 插入聊天记录
     */
    int insert(AiChatMessage message);

    /**
     * 获取用户最近的聊天记录
     */
    List<AiChatMessage> selectRecentByUserId(@Param("userId") Long userId,
                                              @Param("limit") Integer limit);

    /**
     * 获取用户所有聊天记录
     */
    List<AiChatMessage> selectByUserId(@Param("userId") Long userId);

    /**
     * 删除用户所有聊天记录
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 获取用户聊天次数（今日）
     */
    int countTodayByUserId(@Param("userId") Long userId);
}
