package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.UserMessage;
import com.moodlater.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    /**
     * 发送留言
     */
    @PostMapping("/send")
    public Result<Map<String, Object>> sendMessage(@RequestBody Map<String, Object> params, @RequestAttribute("userId") Long userId) {
        Long toUserId = Long.valueOf(params.get("toUserId").toString());
        String content = params.get("content").toString();
        if (content == null || content.trim().isEmpty()) {
            return Result.error("留言内容不能为空");
        }
        if (content.length() > 200) {
            return Result.error("留言内容不能超过200个字符");
        }
        try {
            Map<String, Object> result = userMessageService.sendMessage(userId, toUserId, content.trim());
            return Result.success(result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 发送抱抱
     */
    @PostMapping("/hug")
    public Result<Map<String, Object>> sendHug(@RequestBody Map<String, Object> params, @RequestAttribute("userId") Long userId) {
        Long toUserId = Long.valueOf(params.get("toUserId").toString());
        try {
            Map<String, Object> result = userMessageService.sendHug(userId, toUserId);
            return Result.success(result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取收到的留言列表
     */
    @GetMapping("/received")
    public Result<List<UserMessage>> getReceivedMessages(@RequestAttribute("userId") Long userId) {
        List<UserMessage> messages = userMessageService.getReceivedMessages(userId);
        return Result.success(messages);
    }

    /**
     * 获取发出的留言列表
     */
    @GetMapping("/sent")
    public Result<List<UserMessage>> getSentMessages(@RequestAttribute("userId") Long userId) {
        List<UserMessage> messages = userMessageService.getSentMessages(userId);
        return Result.success(messages);
    }

    /**
     * 获取未读留言数量
     */
    @GetMapping("/unread-count")
    public Result<Integer> getUnreadCount(@RequestAttribute("userId") Long userId) {
        int count = userMessageService.getUnreadCount(userId);
        return Result.success(count);
    }

    /**
     * 标记为已读
     */
    @PutMapping("/read/{messageId}")
    public Result<Void> markAsRead(@PathVariable Long messageId, @RequestAttribute("userId") Long userId) {
        userMessageService.markAsRead(userId, messageId);
        return Result.success();
    }

    /**
     * 全部标记为已读
     */
    @PutMapping("/read-all")
    public Result<Void> markAllAsRead(@RequestAttribute("userId") Long userId) {
        userMessageService.markAllAsRead(userId);
        return Result.success();
    }
}
