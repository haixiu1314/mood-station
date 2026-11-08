package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.service.AiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 情绪小喵 AI 聊天接口
 */
@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    @Autowired
    private AiChatService aiChatService;

    /**
     * 发送消息
     */
    @PostMapping("/chat")
    public Result<Map<String, Object>> chat(@RequestBody Map<String, String> request,
                                             @RequestAttribute("userId") Long userId) {
        String message = request.get("message");
        if (message == null || message.trim().isEmpty()) {
            return Result.error("消息不能为空");
        }
        if (message.length() > 1000) {
            return Result.error("消息长度不能超过1000个字符");
        }

        try {
            Map<String, Object> result = aiChatService.sendMessage(userId, message.trim());
            return Result.success(result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("服务异常，请稍后再试");
        }
    }

    /**
     * 获取聊天历史
     */
    @GetMapping("/history")
    public Result<List<Map<String, Object>>> getHistory(@RequestAttribute("userId") Long userId) {
        List<Map<String, Object>> history = aiChatService.getChatHistory(userId);
        return Result.success(history);
    }

    /**
     * 清除聊天历史
     */
    @DeleteMapping("/history")
    public Result<Void> clearHistory(@RequestAttribute("userId") Long userId) {
        aiChatService.clearHistory(userId);
        return Result.success();
    }

    /**
     * 获取今日聊天次数
     */
    @GetMapping("/today-count")
    public Result<Map<String, Integer>> getTodayCount(@RequestAttribute("userId") Long userId) {
        Map<String, Integer> count = aiChatService.getTodayCount(userId);
        return Result.success(count);
    }
}
