package com.moodlater.service;

import com.moodlater.entity.*;
import com.moodlater.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostTrashMapper postTrashMapper;

    @Autowired
    private PostAppealMapper postAppealMapper;

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DailyRewardService dailyRewardService;

    public Map<String, Object> createPost(Post post) {
        postMapper.insert(post);
        Post created = postMapper.selectById(post.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("post", created);
        
        // 每日首次发布帖子奖励（2个心动币）
        int rewardAmount = dailyRewardService.grantReward(post.getUserId(), "daily_post", 2);
        if (rewardAmount > 0) {
            result.put("rewardMessage", "帖子发布成功，获得" + rewardAmount + "个心动币");
            result.put("rewardAmount", rewardAmount);
        }
        
        // 每日首次转发日记奖励（1个心动币）
        if ("diary".equals(post.getSource())) {
            int forwardReward = dailyRewardService.grantReward(post.getUserId(), "daily_forward", 1);
            if (forwardReward > 0) {
                String existingMsg = (String) result.get("rewardMessage");
                if (existingMsg != null) {
                    result.put("rewardMessage", existingMsg + "；转发日记成功，额外获得" + forwardReward + "个心动币");
                    result.put("rewardAmount", (int) result.get("rewardAmount") + forwardReward);
                } else {
                    result.put("rewardMessage", "转发日记成功，获得" + forwardReward + "个心动币");
                    result.put("rewardAmount", forwardReward);
                }
            }
        }
        
        return result;
    }

    public List<Map<String, Object>> getAllPosts(Long userId) {
        List<Post> posts = postMapper.selectAll();
        return buildPostVOList(posts, userId);
    }

    public List<Post> getUserPosts(Long userId) {
        return postMapper.selectByUserId(userId);
    }

    public List<Post> getPostsByDate(Long userId, String date) {
        return postMapper.selectByUserIdAndDate(userId, date);
    }

    public List<Post> searchPosts(Long userId, String keyword) {
        return postMapper.searchByUserIdAndKeyword(userId, keyword);
    }

    public Map<String, Object> getPostById(Long id, Long userId) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            return null;
        }
        return buildPostVO(post, userId);
    }

    @Transactional
    public Map<String, Object> likePost(Long userId, Long postId) {
        LikeRecord record = likeRecordMapper.selectByUserIdAndPostId(userId, postId);
        Map<String, Object> result = new HashMap<>();
        if (record == null) {
            record = new LikeRecord();
            record.setUserId(userId);
            record.setPostId(postId);
            likeRecordMapper.insert(record);
            postMapper.incrementLikeCount(postId);
            
            // 每日首次点赞奖励（不能是自己的帖子）
            Post post = postMapper.selectById(postId);
            if (post != null && !post.getUserId().equals(userId)) {
                int rewardAmount = dailyRewardService.grantReward(userId, "daily_like", 1);
                if (rewardAmount > 0) {
                    result.put("rewardMessage", "点赞成功，获得" + rewardAmount + "个心动币");
                    result.put("rewardAmount", rewardAmount);
                }
                
                // 发送通知给帖子作者
                notificationService.sendNotification(post.getUserId(), "message", "post_like", "帖子被点赞",
                        "有位帅哥或美女给你的帖子点赞了，快去看看", postId);
            }
        }
        return result;
    }

    @Transactional
    public void unlikePost(Long userId, Long postId) {
        LikeRecord record = likeRecordMapper.selectByUserIdAndPostId(userId, postId);
        if (record != null) {
            likeRecordMapper.deleteByUserIdAndPostId(userId, postId);
            postMapper.decrementLikeCount(postId);
        }
    }

    public Map<String, Object> addComment(Comment comment) {
        commentMapper.insert(comment);
        postMapper.incrementCommentCount(comment.getPostId());
        
        Map<String, Object> result = new HashMap<>();
        
        // 每日首次评论奖励（不能是自己的帖子）
        Post post = postMapper.selectById(comment.getPostId());
        if (post != null && !post.getUserId().equals(comment.getUserId())) {
            int rewardAmount = dailyRewardService.grantReward(comment.getUserId(), "daily_comment", 1);
            if (rewardAmount > 0) {
                result.put("rewardMessage", "评论成功，获得" + rewardAmount + "个心动币");
                result.put("rewardAmount", rewardAmount);
            }
            
            // 发送通知给帖子作者
            notificationService.sendNotification(post.getUserId(), "message", "post_comment", "帖子收到评论",
                    "有位帅哥或美女给你的帖子评论了，快去看看", comment.getPostId());
        }
        
        return result;
    }

    public List<Map<String, Object>> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentMapper.selectByPostId(postId);
        return buildCommentVOList(comments);
    }

    @Transactional
    public void deletePost(Long id, Long userId) {
        Post post = postMapper.selectById(id);
        if (post == null) {
            return;
        }

        int trashCount = postTrashMapper.countByUserId(userId);
        if (trashCount >= 10) {
            throw new RuntimeException("回收站已满，最多存储10份删除的帖子");
        }

        PostTrash trash = new PostTrash();
        trash.setUserId(userId);
        trash.setPostId(post.getId());
        trash.setTitle(post.getTitle());
        trash.setEmotion(post.getEmotion());
        trash.setEmotionLabel(post.getEmotionLabel());
        trash.setContent(post.getContent());
        trash.setImages(post.getImages());
        trash.setLikeCount(post.getLikeCount());
        trash.setCommentCount(post.getCommentCount());
        trash.setDeletedBy("user");
        trash.setOriginalCreateTime(post.getCreateTime());
        postTrashMapper.insert(trash);

        postMapper.softDelete(id);
    }

    public List<PostTrash> getTrashPosts(Long userId) {
        return postTrashMapper.selectByUserId(userId);
    }

    @Transactional
    public void restorePost(Long trashId, Long userId) {
        PostTrash trash = postTrashMapper.selectById(trashId);
        if (trash == null || !trash.getUserId().equals(userId)) {
            throw new RuntimeException("回收记录不存在");
        }
        postMapper.restore(trash.getPostId());
        postTrashMapper.deleteById(trashId, userId);
    }

    @Transactional
    public void permanentlyDeletePost(Long trashId, Long userId) {
        PostTrash trash = postTrashMapper.selectById(trashId);
        if (trash == null || !trash.getUserId().equals(userId)) {
            throw new RuntimeException("回收记录不存在");
        }
        postMapper.deleteById(trash.getPostId());
        postTrashMapper.deleteById(trashId, userId);
    }

    @Transactional
    public void submitAppeal(Long userId, Long postId, String reason) {
        PostTrash trash = postTrashMapper.selectById(postId);
        if (trash == null || !trash.getUserId().equals(userId)) {
            throw new RuntimeException("帖子不存在");
        }
        if (!"admin".equals(trash.getDeletedBy())) {
            throw new RuntimeException("只有管理员删除的帖子才能申诉");
        }
        if (trash.getAppealStatus() != null) {
            throw new RuntimeException("该帖子已申诉过，请等待审核");
        }

        // 检查是否已有待审核的申诉
        List<PostAppeal> existing = postAppealMapper.selectByUserId(userId);
        for (PostAppeal appeal : existing) {
            if (appeal.getPostId().equals(trash.getPostId()) && appeal.getStatus() == 0) {
                throw new RuntimeException("该帖子已有待审核的申诉");
            }
        }

        PostAppeal appeal = new PostAppeal();
        appeal.setUserId(userId);
        appeal.setPostId(trash.getPostId());
        appeal.setPostTitle(trash.getTitle());
        appeal.setPostContent(trash.getContent());
        appeal.setReason(reason);
        postAppealMapper.insert(appeal);

        // 标记回收站为申诉中（用status=0表示）
        postTrashMapper.updateAppealStatus(postId, 0);
    }

    public List<PostAppeal> getUserAppeals(Long userId) {
        return postAppealMapper.selectByUserId(userId);
    }

    private List<Map<String, Object>> buildPostVOList(List<Post> posts, Long userId) {
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (Post post : posts) {
            result.add(buildPostVO(post, userId));
        }
        return result;
    }

    private Map<String, Object> buildPostVO(Post post, Long userId) {
        Map<String, Object> vo = new HashMap<>();
        vo.put("id", post.getId());
        vo.put("title", post.getTitle());
        vo.put("emotion", post.getEmotion());
        vo.put("emotionLabel", post.getEmotionLabel());
        vo.put("content", post.getContent());
        vo.put("images", post.getImages());
        vo.put("likeCount", post.getLikeCount());
        vo.put("commentCount", post.getCommentCount());
        vo.put("source", post.getSource());
        vo.put("createTime", post.getCreateTime());
        User author = userMapper.selectById(post.getUserId());
        Map<String, Object> authorInfo = new HashMap<>();
        if (author != null) {
            authorInfo.put("id", author.getId());
            authorInfo.put("avatar", author.getAvatarUrl());
            authorInfo.put("nickname", author.getNickname());
        }
        vo.put("author", authorInfo);
        
        if (userId != null) {
            LikeRecord record = likeRecordMapper.selectByUserIdAndPostId(userId, post.getId());
            vo.put("isLiked", record != null);
        } else {
            vo.put("isLiked", false);
        }
        
        return vo;
    }

    private List<Map<String, Object>> buildCommentVOList(List<Comment> comments) {
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (Comment comment : comments) {
            Map<String, Object> vo = new HashMap<>();
            vo.put("id", comment.getId());
            vo.put("content", comment.getContent());
            vo.put("createTime", comment.getCreateTime());
            User user = userMapper.selectById(comment.getUserId());
            Map<String, Object> userInfo = new HashMap<>();
            if (user != null) {
                userInfo.put("avatar", user.getAvatarUrl());
                userInfo.put("nickname", user.getNickname());
            }
            vo.put("user", userInfo);
            result.add(vo);
        }
        return result;
    }
}
