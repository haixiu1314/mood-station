package com.moodlater.service;

import com.moodlater.entity.Comment;
import com.moodlater.entity.Post;
import com.moodlater.entity.PostAppeal;
import com.moodlater.entity.PostTrash;
import com.moodlater.mapper.AdminPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminPostService {

    @Autowired
    private AdminPostMapper adminPostMapper;

    @Autowired
    private NotificationService notificationService;

    public List<Post> listAllPosts() {
        return adminPostMapper.selectAllPosts();
    }

    public Post getPostById(Long id) {
        return adminPostMapper.selectPostById(id);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = adminPostMapper.selectPostById(id);
        if (post == null) {
            return;
        }

        // 软删除帖子
        adminPostMapper.softDeletePost(id);

        // 同步写入回收站，标记为管理员删除
        PostTrash trash = new PostTrash();
        trash.setUserId(post.getUserId());
        trash.setPostId(post.getId());
        trash.setTitle(post.getTitle());
        trash.setEmotion(post.getEmotion());
        trash.setEmotionLabel(post.getEmotionLabel());
        trash.setContent(post.getContent());
        trash.setImages(post.getImages());
        trash.setLikeCount(post.getLikeCount());
        trash.setCommentCount(post.getCommentCount());
        trash.setDeletedBy("admin");
        trash.setDeletedByName("系统管理员");
        trash.setOriginalCreateTime(post.getCreateTime());
        adminPostMapper.insertTrash(trash);
        
        // 发送通知
        notificationService.sendNotification(post.getUserId(), "system", "post_delete", "帖子被删除",
                "你的帖子存在违规行为，请检查后再重新发布哦，也可以进行申诉哦w(ﾟДﾟ)w", post.getId());
    }

    @Transactional
    public void batchDeletePosts(List<Long> ids) {
        for (Long id : ids) {
            deletePost(id);
        }
    }

    public List<Comment> listAllComments() {
        return adminPostMapper.selectAllComments();
    }

    public Comment getCommentById(Long id) {
        return adminPostMapper.selectCommentById(id);
    }

    @Transactional
    public void deleteComment(Long id) {
        adminPostMapper.deleteComment(id);
    }

    @Transactional
    public void batchDeleteComments(List<Long> ids) {
        adminPostMapper.batchDeleteComments(ids);
    }

    // 申诉管理
    public List<PostAppeal> listPendingAppeals() {
        return adminPostMapper.selectAllPendingAppeals();
    }

    public PostAppeal getAppealById(Long id) {
        return adminPostMapper.selectAppealById(id);
    }

    @Transactional
    public void reviewAppeal(Long id, Integer status, Long reviewerId) {
        PostAppeal appeal = adminPostMapper.selectAppealById(id);
        if (appeal == null) {
            throw new RuntimeException("申诉记录不存在");
        }

        // 更新申诉状态
        adminPostMapper.updateAppealStatus(id, status, reviewerId);

        if (status == 1) {
            // 申诉通过：恢复帖子（is_deleted=0），从回收站移除
            adminPostMapper.restorePost(appeal.getPostId());
            adminPostMapper.deleteTrashByPostId(appeal.getPostId());
            // 发送通知
            notificationService.sendNotification(appeal.getUserId(), "system", "appeal_success", "申诉成功",
                    "您的帖子申诉成功了，是我有眼不识泰山，给您带来不便了，希望得到您的谅解┭┮﹏┭┮", appeal.getPostId());
        } else if (status == 2) {
            // 申诉不通过：标记回收站申诉不通过
            adminPostMapper.updateTrashAppealStatus(appeal.getPostId(), 2);
            // 发送通知
            notificationService.sendNotification(appeal.getUserId(), "system", "appeal_fail", "申诉失败",
                    "您的帖子申诉失败了，确实存在违规，没办法恢复，抱歉哈╮(๑•́ ₃•̀๑)╭", appeal.getPostId());
        }
    }

    public int countPendingAppeals() {
        return adminPostMapper.countPendingAppeals();
    }
}
