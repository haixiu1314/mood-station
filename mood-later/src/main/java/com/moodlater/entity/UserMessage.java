package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserMessage {
    private Long id;
    private Long fromUserId;
    private String fromNickname;
    private String fromAvatar;
    private Long toUserId;
    private String content;
    private Integer isRead;
    private LocalDateTime createTime;
}
