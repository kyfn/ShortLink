package org.lbc.shortlink.admin.config;

import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.admin.common.biz.HeaderCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

    private final HeaderCheckInterceptor headerCheckInterceptor;

    private final String[] excludePaths = {
            "/api/slink/v1/user/login",
            "/api/slink/v1/user/register",
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePaths);
    }
}
