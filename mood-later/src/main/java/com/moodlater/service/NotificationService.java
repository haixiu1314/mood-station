package com.moodlater.service;

import com.moodlater.entity.Notification;
import com.moodlater.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Notification> getUserNotifications(Long userId, String category) {
        return notificationMapper.selectByUserId(userId, category);
    }

    public int getUnreadCount(Long userId) {
        return notificationMapper.countUnread(userId);
    }

    public int getUnreadCountByType(Long userId, String category) {
        return notificationMapper.countUnreadByCategory(userId, category);
    }

    public void markAsRead(Long userId, Long notificationId) {
        notificationMapper.markAsRead(notificationId, userId);
    }

    public void markAllAsRead(Long userId) {
        notificationMapper.markAllAsRead(userId);
    }

    public void markAllAsReadByType(Long userId, String category) {
        notificationMapper.markAllAsReadByCategory(userId, category);
    }
}
