package com.moodlater.service;

import com.moodlater.entity.User;
import com.moodlater.mapper.AdminMapper;
import com.moodlater.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, Object> adminLogin(String username, String password) {
        User admin = adminMapper.selectAdminByUsername(username);
        if (admin == null || !password.equals(admin.getPassword())) {
            return null;
        }
        String token = jwtUtil.generateToken(admin.getId(), admin.getOpenid());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("adminId", admin.getId());
        result.put("nickname", admin.getNickname());
        return result;
    }

    public List<User> getUserList() {
        return adminMapper.selectAllUsers();
    }

    public User getUserById(Long id) {
        return adminMapper.selectUserById(id);
    }

    @Transactional
    public void banUser(Long id) {
        adminMapper.updateUserStatus(id, "banned");
    }

    @Transactional
    public void unbanUser(Long id) {
        adminMapper.updateUserStatus(id, "normal");
    }

    @Transactional
    public void deleteUser(Long id) {
        adminMapper.deleteUserById(id);
    }

    @Transactional
    public void giftCoins(Long userId, Integer amount) {
        adminMapper.updateCoinBalance(userId, amount);
    }
}
