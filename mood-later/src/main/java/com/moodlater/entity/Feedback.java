package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Feedback {
    private Long id;
    private Long userId;
    private String nickname;
    private String content;
    private LocalDateTime createTime;
}
