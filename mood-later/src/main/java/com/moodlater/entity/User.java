package com.moodlater.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String openid;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private Integer gender;
    private java.time.LocalDate birthday;
    private String bio;
    private Integer coinBalance;
    private String status;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
