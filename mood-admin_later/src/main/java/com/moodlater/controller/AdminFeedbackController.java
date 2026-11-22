package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.Feedback;
import com.moodlater.service.AdminFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/feedback")
public class AdminFeedbackController {

    @Autowired
    private AdminFeedbackService feedbackService;

    @GetMapping("/list")
    public Result<List<Feedback>> list() {
        return Result.success(feedbackService.listAll());
    }

    @GetMapping("/{id}")
    public Result<Feedback> getById(@PathVariable Long id) {
        return Result.success(feedbackService.getById(id));
    }
}
