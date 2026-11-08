package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.User;
import com.moodlater.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    @Autowired
    private AdminUserService userService;

    @GetMapping("/list")
    public Result<List<User>> list() {
        return Result.success(userService.listAllUsers());
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @PutMapping("/ban/{id}")
    public Result<Void> ban(@PathVariable Long id) {
        userService.banUser(id);
        return Result.success();
    }

    @PutMapping("/unban/{id}")
    public Result<Void> unban(@PathVariable Long id) {
        userService.unbanUser(id);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @PostMapping("/gift-coins")
    public Result<Void> giftCoins(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long userId = Long.valueOf(params.get("userId").toString());
        Integer amount = Integer.valueOf(params.get("amount").toString());
        String reason = params.get("reason") != null ? params.get("reason").toString() : "管理员赠送";
        Long adminId = (Long) request.getAttribute("adminId");
        userService.giftCoins(userId, amount, adminId, reason);
        return Result.success();
    }
}
