package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Banner {
    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer sortOrder;
    private Integer isActive;
    private LocalDateTime createTime;
}
