package com.moodlater.service;

import com.moodlater.entity.HealingQuote;
import com.moodlater.entity.User;
import com.moodlater.entity.UserMessage;
import com.moodlater.mapper.HealingQuoteMapper;
import com.moodlater.mapper.UserMapper;
import com.moodlater.mapper.UserMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserMessageService {

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private HealingQuoteMapper healingQuoteMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    /**
     * 发送留言
     */
    @Transactional
    public Map<String, Object> sendMessage(Long fromUserId, Long toUserId, String content) {
        if (fromUserId.equals(toUserId)) {
            throw new RuntimeException("不能给自己留言");
        }

        User fromUser = userMapper.selectById(fromUserId);
        User toUser = userMapper.selectById(toUserId);
        if (fromUser == null || toUser == null) {
            throw new RuntimeException("用户不存在");
        }

        UserMessage message = new UserMessage();
        message.setFromUserId(fromUserId);
        message.setFromNickname(fromUser.getNickname());
        message.setFromAvatar(fromUser.getAvatarUrl());
        message.setToUserId(toUserId);
        message.setContent(content);
        userMessageMapper.insert(message);

        // 发送通知
        notificationService.sendNotification(toUserId, "message", "user_message", "收到留言",
                fromUser.getNickname() + "给你留言了：" + content, message.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("message", "留言成功");
        return result;
    }

    /**
     * 发送抱抱（随机暖评）
     */
    @Transactional
    public Map<String, Object> sendHug(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId)) {
            throw new RuntimeException("不能给自己抱抱");
        }

        User fromUser = userMapper.selectById(fromUserId);
        User toUser = userMapper.selectById(toUserId);
        if (fromUser == null || toUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 随机获取一条暖评
        HealingQuote quote = healingQuoteMapper.selectRandomActive("hug");
        String hugContent = quote != null ? quote.getContent() : "给你一个温暖的抱抱~";

        UserMessage message = new UserMessage();
        message.setFromUserId(fromUserId);
        message.setFromNickname(fromUser.getNickname());
        message.setFromAvatar(fromUser.getAvatarUrl());
        message.setToUserId(toUserId);
        message.setContent("[抱抱] " + hugContent);
        userMessageMapper.insert(message);

        // 发送通知
        notificationService.sendNotification(toUserId, "message", "user_hug", "收到抱抱",
                fromUser.getNickname() + "抱抱你，并对你说：" + hugContent, message.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("message", "抱抱发送成功");
        result.put("hugContent", hugContent);
        return result;
    }

    /**
     * 获取收到的留言列表
     */
    public List<UserMessage> getReceivedMessages(Long userId) {
        return userMessageMapper.selectByToUserId(userId);
    }

    /**
     * 获取发出的留言列表
     */
    public List<UserMessage> getSentMessages(Long userId) {
        return userMessageMapper.selectByFromUserId(userId);
    }

    /**
     * 获取未读留言数量
     */
    public int getUnreadCount(Long userId) {
        return userMessageMapper.countUnread(userId);
    }

    /**
     * 标记为已读
     */
    public void markAsRead(Long userId, Long messageId) {
        userMessageMapper.markAsRead(messageId, userId);
    }

    /**
     * 全部标记为已读
     */
    public void markAllAsRead(Long userId) {
        userMessageMapper.markAllAsRead(userId);
    }
}
