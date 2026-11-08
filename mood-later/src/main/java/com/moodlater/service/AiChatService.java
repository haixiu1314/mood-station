package com.moodlater.service;

import com.moodlater.entity.AiChatMessage;
import com.moodlater.mapper.AiChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AiChatService {

    @Autowired
    private AiChatMapper aiChatMapper;

    @Autowired
    private DeepSeekService deepSeekService;

    // 每日聊天次数限制
    private static final int DAILY_LIMIT = 50;

    // 每次对话携带的历史消息数量
    private static final int HISTORY_SIZE = 10;

    /**
     * 发送消息并获取 AI 回复
     */
    public Map<String, Object> sendMessage(Long userId, String userMessage) throws Exception {
        // 检查每日次数限制
        int todayCount = aiChatMapper.countTodayByUserId(userId);
        if (todayCount >= DAILY_LIMIT) {
            throw new RuntimeException("今日聊天次数已达上限（" + DAILY_LIMIT + "次），明天再来找小喵吧~");
        }

        // 保存用户消息
        AiChatMessage userMsg = new AiChatMessage();
        userMsg.setUserId(userId);
        userMsg.setRole("user");
        userMsg.setContent(userMessage);
        userMsg.setEmotion(null);
        aiChatMapper.insert(userMsg);

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

        // 调用 DeepSeek API
        String aiReply;
        try {
            aiReply = deepSeekService.chat(messages);
        } catch (Exception e) {
            throw new RuntimeException("小喵暂时走神了，请稍后再试~ （" + e.getMessage() + "）");
        }

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
        result.put("todayCount", todayCount + 1);
        result.put("dailyLimit", DAILY_LIMIT);

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

    /**
     * 获取今日聊天次数
     */
    public Map<String, Integer> getTodayCount(Long userId) {
        int count = aiChatMapper.countTodayByUserId(userId);
        Map<String, Integer> result = new HashMap<>();
        result.put("todayCount", count);
        result.put("dailyLimit", DAILY_LIMIT);
        return result;
    }
}
