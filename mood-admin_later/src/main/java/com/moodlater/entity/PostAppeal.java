package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostAppeal {
    private Long id;
    private Long userId;
    private Long postId;
    private String postTitle;
    private String postContent;
    private String reason;
    private Integer status;
    private LocalDateTime appealTime;
    private LocalDateTime reviewTime;
    private Long reviewerId;
    // 关联查询字段
    private String userNickname;
    private String userAvatar;
}
