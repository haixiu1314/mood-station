package com.moodlater.entity;

import lombok.Data;

@Data
public class ExpressCompany {
    private Long id;
    private String name;
    private String code;
    private String placeholder;
    private Integer sortOrder;
}
