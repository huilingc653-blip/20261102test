package com.bytter.core.autoconfig;

import com.bytter.core.common.context.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BytterWebMvcConfig implements WebMvcConfigurer {

    // TODO here 不需要设置用户信息的url 后续取白名单列表
    private static final String[] EXCLUDE_PATHS = {"/static/**", "/login", "/oauth/**", "/oauth2/**", "/userLoginType","/swagger-ui.html"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor()).excludePathPatterns(EXCLUDE_PATHS);
    }

    @Bean
    public UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }


}
