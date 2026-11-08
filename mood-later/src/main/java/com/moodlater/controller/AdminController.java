package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.User;
import com.moodlater.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        if (username == null || password == null) {
            return Result.error("用户名和密码不能为空");
        }
        Map<String, Object> result = adminService.adminLogin(username.trim(), password);
        if (result == null) {
            return Result.error("管理员账号或密码错误");
        }
        return Result.success(result);
    }

    @GetMapping("/user/list")
    public Result<List<User>> getUserList() {
        List<User> users = adminService.getUserList();
        return Result.success(users);
    }

    @GetMapping("/user/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = adminService.getUserById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    @PutMapping("/user/ban/{id}")
    public Result<Void> banUser(@PathVariable Long id) {
        adminService.banUser(id);
        return Result.success();
    }

    @PutMapping("/user/unban/{id}")
    public Result<Void> unbanUser(@PathVariable Long id) {
        adminService.unbanUser(id);
        return Result.success();
    }

    @DeleteMapping("/user/delete/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return Result.success();
    }

    @PostMapping("/user/gift-coins")
    public Result<Void> giftCoins(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        Integer amount = Integer.valueOf(params.get("amount").toString());
        if (amount <= 0) {
            return Result.error("赠送数量必须为正数");
        }
        adminService.giftCoins(userId, amount);
        return Result.success();
    }
}
