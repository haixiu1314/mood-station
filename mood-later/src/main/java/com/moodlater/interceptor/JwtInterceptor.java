package com.moodlater.interceptor;

import com.moodlater.entity.User;
import com.moodlater.mapper.UserMapper;
import com.moodlater.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (token == null || jwtUtil.isTokenExpired(token)) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\"}");
            return false;
        }
        Long userId = jwtUtil.getUserId(token);
        User user = userMapper.selectById(userId);
        if (user != null && "banned".equals(user.getStatus())) {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"message\":\"您已被封号\",\"banned\":true}");
            return false;
        }
        request.setAttribute("userId", userId);
        
        String newToken = jwtUtil.refreshToken(token);
        if (newToken != null) {
            response.setHeader("Authorization", "Bearer " + newToken);
        }
        
        return true;
    }
}
