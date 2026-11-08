package com.moodlater.mapper;

import com.moodlater.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PostMapper {

    int insert(Post post);

    Post selectById(@Param("id") Long id);

    List<Post> selectAll();

    List<Post> selectByUserId(@Param("userId") Long userId);

    List<Post> selectByUserIdAndDate(@Param("userId") Long userId, @Param("date") String date);

    List<Post> searchByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);

    int updateById(Post post);

    int deleteById(@Param("id") Long id);

    int softDelete(@Param("id") Long id);

    int restore(@Param("id") Long id);

    int incrementLikeCount(@Param("id") Long id);

    int decrementLikeCount(@Param("id") Long id);

    int incrementCommentCount(@Param("id") Long id);
}
