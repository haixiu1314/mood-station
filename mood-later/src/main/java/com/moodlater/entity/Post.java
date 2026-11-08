package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String emotion;
    private String emotionLabel;
    private String content;
    private String images;
    private Integer likeCount;
    private Integer commentCount;
    private String source;
    private Long sourceDiaryId;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
