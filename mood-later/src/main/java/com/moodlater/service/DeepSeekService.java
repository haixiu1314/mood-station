package com.moodlater.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moodlater.config.DeepSeekConfig;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DeepSeekService {

    @Autowired
    private DeepSeekConfig deepSeekConfig;

    private final OkHttpClient httpClient;
    private final Gson gson = new Gson();

    public DeepSeekService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 系统提示词 - 情绪小喵人格 (Java 8 兼容写法)
     */
    private static final String SYSTEM_PROMPT =
            "你是\"情绪小喵\"，一个温暖治愈的AI情绪陪伴助手。你的职责是：\n" +
            "\n" +
            "1. 倾听用户的情绪和烦恼，给予温暖的回应\n" +
            "2. 提供情绪支持和心理安慰\n" +
            "3. 适当给出建议和鼓励\n" +
            "4. 保持可爱、温柔、治愈的风格\n" +
            "5. 用简短的回复（2-3句话为主），避免长篇大论\n" +
            "6. 可以适当使用emoji让回复更生动\n" +
            "7. 不要主动提及你是AI，保持\"小喵\"的身份\n" +
            "\n" +
            "回复风格：温暖、治愈、可爱、简短、有同理心";

    /**
     * 发送聊天消息并获取回复
     */
    public String chat(List<Map<String, String>> messages) throws IOException {
        // 构建消息数组，在最前面加入系统提示词
        JsonArray messagesArray = new JsonArray();

        // 添加系统提示词
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", SYSTEM_PROMPT);
        messagesArray.add(systemMessage);

        // 添加用户消息历史
        for (Map<String, String> msg : messages) {
            JsonObject message = new JsonObject();
            message.addProperty("role", msg.get("role"));
            message.addProperty("content", msg.get("content"));
            messagesArray.add(message);
        }

        // 构建请求体
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", deepSeekConfig.getModel());
        requestBody.add("messages", messagesArray);
        requestBody.addProperty("temperature", deepSeekConfig.getTemperature());
        requestBody.addProperty("max_tokens", deepSeekConfig.getMaxTokens());
        requestBody.addProperty("stream", false);

        // 创建请求
        Request request = new Request.Builder()
                .url(deepSeekConfig.getBaseUrl() + "/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + deepSeekConfig.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        gson.toJson(requestBody),
                        MediaType.parse("application/json")
                ))
                .build();

        // 发送请求
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("DeepSeek API 调用失败: " + response.code() + " " + response.message());
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

            // 解析回复内容
            JsonArray choices = jsonResponse.getAsJsonArray("choices");
            if (choices != null && choices.size() > 0) {
                JsonObject firstChoice = choices.get(0).getAsJsonObject();
                JsonObject message = firstChoice.getAsJsonObject("message");
                return message.get("content").getAsString();
            }

            throw new IOException("DeepSeek API 返回格式异常");
        }
    }
}
