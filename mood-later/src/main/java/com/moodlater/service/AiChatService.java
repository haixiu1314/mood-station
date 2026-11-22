package com.moodlater.service;

import com.moodlater.common.AiException;
import com.moodlater.entity.AiChatMessage;
import com.moodlater.entity.AiConfig;
import com.moodlater.mapper.AiChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AiChatService {

    @Autowired
    private AiChatMapper aiChatMapper;

    @Autowired
    private AiConfigService aiConfigService;

    @Autowired
    private DeepSeekService deepSeekService;

    // 每次对话携带的历史消息数量
    private static final int HISTORY_SIZE = 10;

    /**
     * 发送消息并获取 AI 回复
     */
    public Map<String, Object> sendMessage(Long userId, String userMessage) throws AiException {
        // 读取用户自己的 AI 配置
        AiConfig config = aiConfigService.getConfig(userId);
        if (config == null || isBlank(config.getBaseUrl()) || isBlank(config.getApiKey()) || isBlank(config.getModel())) {
            throw new AiException(4001, "当前模型没有配置，请先配置模型");
        }

        // 获取历史消息用于上下文
        List<AiChatMessage> history = aiChatMapper.selectRecentByUserId(userId, HISTORY_SIZE);
        Collections.reverse(history); // 反转顺序，变为时间正序

        // 构建 messages 数组供 DeepSeek 调用
        List<Map<String, String>> messages = new ArrayList<>();
        for (AiChatMessage msg : history) {
            Map<String, String> message = new HashMap<>();
            message.put("role", msg.getRole());
            message.put("content", msg.getContent());
            messages.add(message);
        }
        Map<String, String> current = new HashMap<>();
        current.put("role", "user");
        current.put("content", userMessage);
        messages.add(current);

        // 调用 AI（使用用户自定义配置）
        String aiReply;
        try {
            aiReply = deepSeekService.chat(config.getBaseUrl(), config.getApiKey(), config.getModel(), messages);
        } catch (Exception e) {
            throw new AiException(4002, "当前模型不可用，请检查一下" + config.getModel());
        }

        // 保存用户消息
        AiChatMessage userMsg = new AiChatMessage();
        userMsg.setUserId(userId);
        userMsg.setRole("user");
        userMsg.setContent(userMessage);
        userMsg.setEmotion(null);
        aiChatMapper.insert(userMsg);

        // 保存 AI 回复
        AiChatMessage aiMsg = new AiChatMessage();
        aiMsg.setUserId(userId);
        aiMsg.setRole("assistant");
        aiMsg.setContent(aiReply);
        aiMsg.setEmotion(null);
        aiChatMapper.insert(aiMsg);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("reply", aiReply);

        return result;
    }

    /**
     * 获取用户聊天历史
     */
    public List<Map<String, Object>> getChatHistory(Long userId) {
        List<AiChatMessage> messages = aiChatMapper.selectByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (AiChatMessage msg : messages) {
            Map<String, Object> item = new HashMap<>();
            item.put("role", msg.getRole());
            item.put("content", msg.getContent());
            item.put("createTime", msg.getCreateTime());
            result.add(item);
        }

        return result;
    }

    /**
     * 清除用户聊天历史
     */
    public void clearHistory(Long userId) {
        aiChatMapper.deleteByUserId(userId);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
