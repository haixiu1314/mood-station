package com.moodlater.mapper;

import com.moodlater.entity.PostAppeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PostAppealMapper {

    int insert(PostAppeal appeal);

    List<PostAppeal> selectByUserId(@Param("userId") Long userId);

    List<PostAppeal> selectAllPending();

    PostAppeal selectById(@Param("id") Long id);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("reviewerId") Long reviewerId);

    int countPending();
}
