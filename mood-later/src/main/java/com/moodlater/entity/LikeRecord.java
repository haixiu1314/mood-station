package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LikeRecord {
    private Long id;
    private Long userId;
    private Long postId;
    private LocalDateTime createTime;
}
