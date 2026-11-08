package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.Notification;
import com.moodlater.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/list")
    public Result<List<Notification>> list(@RequestParam(required = false) String type, @RequestAttribute("userId") Long userId) {
        return Result.success(notificationService.getUserNotifications(userId, type));
    }

    @GetMapping("/unread-count")
    public Result<Integer> unreadCount(@RequestAttribute("userId") Long userId) {
        return Result.success(notificationService.getUnreadCount(userId));
    }

    @GetMapping("/unread-count-by-type")
    public Result<Map<String, Integer>> unreadCountByType(@RequestAttribute("userId") Long userId) {
        int systemUnread = notificationService.getUnreadCountByType(userId, "system");
        int messageUnread = notificationService.getUnreadCountByType(userId, "message");
        Map<String, Integer> result = new HashMap<>();
        result.put("system", systemUnread);
        result.put("message", messageUnread);
        result.put("total", systemUnread + messageUnread);
        return Result.success(result);
    }

    @PutMapping("/read/{id}")
    public Result<Void> markAsRead(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        notificationService.markAsRead(userId, id);
        return Result.success();
    }

    @PutMapping("/read-all")
    public Result<Void> markAllAsRead(@RequestParam(required = false) String type, @RequestAttribute("userId") Long userId) {
        if (type != null) {
            notificationService.markAllAsReadByType(userId, type);
        } else {
            notificationService.markAllAsRead(userId);
        }
        return Result.success();
    }
}
