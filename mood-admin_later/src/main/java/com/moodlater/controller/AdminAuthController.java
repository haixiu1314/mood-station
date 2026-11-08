package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.service.AdminAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    @Autowired
    private AdminAuthService authService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String token = authService.login(username, password);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        return Result.success(data);
    }
}
