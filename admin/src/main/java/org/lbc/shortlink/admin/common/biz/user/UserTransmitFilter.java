package org.lbc.shortlink.admin.common.biz.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.admin.common.convention.exception.ClientException;
import org.lbc.shortlink.admin.common.convention.result.Result;
import org.lbc.shortlink.admin.common.convention.result.Results;
import org.lbc.shortlink.admin.common.enums.UserErrorCodeEnums;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.List;

/**
 * 用户信息传输过滤器
 * 封装用户上下文信息
 */
@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<String> IGNORE_URIS = List.of(
            "POST:/api/slink/v1/admin/user/login",
            "POST:/api/slink/v1/admin/user/register",
            "GET:/api/slink/v1/admin/user/exists"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (!isIgnore(httpServletRequest)) {
            String username = httpServletRequest.getHeader("username");
            String token = httpServletRequest.getHeader("token");
            if (!StrUtil.isAllNotBlank(username, token)) {
                unauthorized(httpServletResponse);
                return;
            }

            try {
                Object userJsonStrObj = stringRedisTemplate.opsForHash().get("login_" + username, token);
                if (userJsonStrObj != null) {
                    UserInfoDTO userInfoDTO = JSON.parseObject(userJsonStrObj.toString(), UserInfoDTO.class);
                    UserContext.setUser(userInfoDTO);
                } else {
                    unauthorized(httpServletResponse);
                    return;
                }
            } catch (Exception e) {
                unauthorized(httpServletResponse);
                return;
            }
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }
    private boolean isIgnore(HttpServletRequest request) {
        String key = request.getMethod() + ":" + request.getRequestURI();
        return IGNORE_URIS.contains(key);
    }
    private void unauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Result<Void> result = Results.failure(new ClientException(UserErrorCodeEnums.USER_TOKEN_ERROR));
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
