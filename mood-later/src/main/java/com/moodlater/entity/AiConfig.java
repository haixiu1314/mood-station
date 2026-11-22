package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AiConfig {
    private Long id;
    private Long userId;
    private String baseUrl;
    private String apiKey;
    private String model;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
