package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HealingQuote {
    private Long id;
    private String content;
    private String author;
    private String category;
    private Integer isActive;
    private LocalDateTime createTime;
}
