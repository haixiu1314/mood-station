package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AiChatMessage {
    private Long id;
    private Long userId;
    private String role; // user / assistant / system
    private String content;
    private String emotion; // happy / sad / angry / calm / anxious
    private LocalDateTime createTime;
}
