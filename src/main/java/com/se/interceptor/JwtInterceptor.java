package com.se.interceptor;

import com.se.exception.userException.UserBizException;
import com.se.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return true;
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            throw new UserBizException("未登录，请先登录");
        }

        try {
            Claims claims = JwtUtil.parseToken(token);
            String userId = claims.getSubject();
            // 将 userId 存到 request，便于后续 Controller 获取
            request.setAttribute("userId", userId);
        } catch (JwtException e) {
            throw new UserBizException("登录已过期或无效，请重新登录");
        }

        return true;
    }
}
