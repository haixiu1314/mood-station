package com.moodlater.mapper;

import com.moodlater.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CommentMapper {

    int insert(Comment comment);

    List<Comment> selectByPostId(@Param("postId") Long postId);

    int deleteById(@Param("id") Long id);
}
