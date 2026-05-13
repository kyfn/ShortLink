package org.lbc.shortlink.admin.common.biz;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.lbc.shortlink.admin.common.convention.exception.ClientException;
import org.lbc.shortlink.admin.common.convention.result.Results;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HeaderCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String userId = request.getHeader("username");
        String token = request.getHeader("token");

        if (StrUtil.isBlank(userId) || StrUtil.isBlank(token)) {
            // 直接返回错误，不进入 Controller
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONUtil.toJsonStr(
                    Results.failure(new ClientException("请求头缺少必要参数"))
            ));
            return false;
        }
        return true;
    }
}
