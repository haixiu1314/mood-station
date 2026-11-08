package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostTrash {
    private Long id;
    private Long userId;
    private Long postId;
    private String title;
    private String emotion;
    private String emotionLabel;
    private String content;
    private String images;
    private Integer likeCount;
    private Integer commentCount;
    private String deletedBy;
    private String deletedByName;
    private LocalDateTime originalCreateTime;
    private LocalDateTime deletedTime;
    private LocalDateTime expireTime;
    // 申诉审核状态: null未申诉 1申诉通过 2申诉不通过
    private Integer appealStatus;
}
