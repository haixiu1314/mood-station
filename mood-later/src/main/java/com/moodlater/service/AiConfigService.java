package com.moodlater.service;

import com.moodlater.entity.AiConfig;
import com.moodlater.mapper.AiConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiConfigService {

    @Autowired
    private AiConfigMapper aiConfigMapper;

    public AiConfig getConfig(Long userId) {
        return aiConfigMapper.selectByUserId(userId);
    }

    public void saveConfig(Long userId, String baseUrl, String apiKey, String model) {
        AiConfig existing = aiConfigMapper.selectByUserId(userId);
        if (existing == null) {
            AiConfig config = new AiConfig();
            config.setUserId(userId);
            config.setBaseUrl(baseUrl);
            config.setApiKey(apiKey);
            config.setModel(model);
            aiConfigMapper.insert(config);
        } else {
            existing.setBaseUrl(baseUrl);
            existing.setApiKey(apiKey);
            existing.setModel(model);
            aiConfigMapper.updateByUserId(existing);
        }
    }
}
