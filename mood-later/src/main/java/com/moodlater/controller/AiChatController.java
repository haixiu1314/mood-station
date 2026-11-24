package com.moodlater.controller;

import com.moodlater.common.AiException;
import com.moodlater.common.Result;
import com.moodlater.entity.AiConfig;
import com.moodlater.service.AiChatService;
import com.moodlater.service.AiConfigService;
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

    @Autowired
    private AiConfigService aiConfigService;

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
        } catch (AiException e) {
            return Result.error(e.getCode(), e.getMessage());
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
     * 获取当前用户的 AI 配置
     */
    @GetMapping("/config")
    public Result<AiConfig> getConfig(@RequestAttribute("userId") Long userId) {
        AiConfig config = aiConfigService.getConfig(userId);
        return Result.success(config);
    }

    /**
     * 保存当前用户的 AI 配置
     */
    @PostMapping("/config")
    public Result<Void> saveConfig(@RequestAttribute("userId") Long userId,
                                   @RequestBody Map<String, String> request) {
        String baseUrl = request.get("baseUrl");
        String apiKey = request.get("apiKey");
        String model = request.get("model");
        if (baseUrl == null || baseUrl.trim().isEmpty()
                || apiKey == null || apiKey.trim().isEmpty()
                || model == null || model.trim().isEmpty()) {
            return Result.error("url、key 和 model 都不能为空");
        }
        aiConfigService.saveConfig(userId, baseUrl.trim(), apiKey.trim(), model.trim());
        return Result.success();
    }
}
