package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DiaryTrash {
    private Long id;
    private Long userId;
    private Long diaryId;
    private String emotion;
    private String emotionLabel;
    private String title;
    private String content;
    private String images;
    private Integer isPublic;
    private LocalDateTime originalCreateTime;
    private LocalDateTime deletedTime;
    private LocalDateTime expireTime;
}
