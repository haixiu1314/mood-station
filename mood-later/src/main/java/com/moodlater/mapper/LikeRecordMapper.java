package com.moodlater.mapper;

import com.moodlater.entity.LikeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeRecordMapper {

    int insert(LikeRecord likeRecord);

    int deleteByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);

    LikeRecord selectByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
}
