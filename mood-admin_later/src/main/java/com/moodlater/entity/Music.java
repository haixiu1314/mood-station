package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Music {
    private Long id;
    private String name;
    private String singer;
    private String type;
    private String fileUrl;
    private Integer isActive;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}