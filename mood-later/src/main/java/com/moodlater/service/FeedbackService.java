package com.moodlater.service;

import com.moodlater.entity.Feedback;
import com.moodlater.entity.User;
import com.moodlater.mapper.FeedbackMapper;
import com.moodlater.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 提交用户反馈
     */
    @Transactional
    public void submit(Long userId, String content) {
        User user = userMapper.selectById(userId);
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setNickname(user != null ? user.getNickname() : null);
        feedback.setContent(content);
        feedbackMapper.insert(feedback);
    }
}
