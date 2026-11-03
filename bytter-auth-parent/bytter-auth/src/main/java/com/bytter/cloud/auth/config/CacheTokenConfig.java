package com.bytter.cloud.auth.config;
import com.bytter.cloud.auth.cache.LoginLockCache;
import com.bytter.cloud.auth.cache.LoginTokenCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 登录时的token配置
 *
 * @author zhoul 2021-03-05
 * @since 1.0.0
 */
@Configuration
public class CacheTokenConfig {
    @Bean
    public LoginTokenCache loginTokenCache() {
        return new LoginTokenCache();
    }
    @Bean
    public LoginLockCache loginLockCache() {
        return new LoginLockCache();
    }
}
