package com.moodlater.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String name;
    private String coverImage;
    private String images;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String description;
    private String tags;
    private String specs;
    private Integer sortOrder;
    private Integer status;
    private Integer stock;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
