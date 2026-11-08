package com.moodlater.service;

import com.moodlater.entity.Admin;
import com.moodlater.mapper.AdminMapper;
import com.moodlater.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String username, String password) {
        Admin admin = adminMapper.selectByUsername(username);
        if (admin == null) {
            throw new RuntimeException("管理员账号不存在");
        }
        if (admin.getStatus() != null && admin.getStatus() == 0) {
            throw new RuntimeException("管理员账号已被禁用");
        }
        if (!password.equals(admin.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        return jwtUtil.generateAdminToken(admin.getId(), admin.getUsername());
    }
}
