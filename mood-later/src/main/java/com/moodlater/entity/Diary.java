package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Diary {
    private Long id;
    private Long userId;
    private String emotion;
    private String emotionLabel;
    private String title;
    private String content;
    private String images;
    private Integer isPublic;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer isDeleted;
}
