package com.moodlater.service;

import com.moodlater.entity.Notification;
import com.moodlater.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    public void sendNotification(Long userId, String category, String type, String title, String content, Long relatedId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setCategory(category);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notificationMapper.insert(notification);
    }
}
