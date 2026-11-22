package com.moodlater.service;

import com.moodlater.entity.Feedback;
import com.moodlater.mapper.AdminFeedbackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminFeedbackService {

    @Autowired
    private AdminFeedbackMapper feedbackMapper;

    public List<Feedback> listAll() {
        return feedbackMapper.selectAll();
    }

    public Feedback getById(Long id) {
        return feedbackMapper.selectById(id);
    }
}
