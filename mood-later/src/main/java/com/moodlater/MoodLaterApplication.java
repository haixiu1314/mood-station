package com.moodlater;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.moodlater.mapper")
public class MoodLaterApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoodLaterApplication.class, args);
    }
}
