package com.moodlater.mapper;

import com.moodlater.entity.PostTrash;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PostTrashMapper {

    int insert(PostTrash postTrash);

    PostTrash selectById(@Param("id") Long id);

    List<PostTrash> selectByUserId(@Param("userId") Long userId);

    int deleteById(@Param("id") Long id, @Param("userId") Long userId);

    int deleteExpired();

    int countByUserId(@Param("userId") Long userId);

    int updateAppealStatus(@Param("postId") Long postId, @Param("appealStatus") Integer appealStatus);
}
