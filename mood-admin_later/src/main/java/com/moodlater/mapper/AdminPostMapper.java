package com.moodlater.mapper;

import com.moodlater.entity.Comment;
import com.moodlater.entity.Post;
import com.moodlater.entity.PostAppeal;
import com.moodlater.entity.PostTrash;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminPostMapper {
    List<Post> selectAllPosts();
    Post selectPostById(@Param("id") Long id);
    int softDeletePost(@Param("id") Long id);
    int batchSoftDeletePosts(@Param("ids") List<Long> ids);

    List<Comment> selectAllComments();
    Comment selectCommentById(@Param("id") Long id);
    int deleteComment(@Param("id") Long id);
    int batchDeleteComments(@Param("ids") List<Long> ids);

    // 申诉管理
    List<PostAppeal> selectAllPendingAppeals();
    PostAppeal selectAppealById(@Param("id") Long id);
    int updateAppealStatus(@Param("id") Long id, @Param("status") Integer status, @Param("reviewerId") Long reviewerId);
    int countPendingAppeals();

    // 回收站
    int insertTrash(PostTrash trash);
    PostTrash selectTrashByPostId(@Param("postId") Long postId);
    int deleteTrashByPostId(@Param("postId") Long postId);
    int updateTrashAppealStatus(@Param("postId") Long postId, @Param("appealStatus") Integer appealStatus);
    int restorePost(@Param("id") Long id);
}
