package com.bytter.cloud.auth.cache;
/**
 * Auth缓存通用常量
 *
 * @author mfs 2022-08-26
 * @since 1.0.0
 */
public interface AuthCacheKey {
    /**
     * 图形码缓存KEY前缀
     */
    String CAPTCHA_CODE_CACHE_PREFIX = "system:captcha:pic:";
    /**
     * 手机验证码缓存KEY前缀
     */
    String SMS_CODE_CACHE_PREFIX = "system:captcha:code:";
    /**
     * 密码错误上限锁定时间
     */
    String LOGIN_LOCK_TIME = "auth:oauth:loginLock:time:";
    /**
     * 密码错误次数
     */
    String LOGIN_LOCK = "auth:oauth:loginLock:";
    /**
     * 用户登陆密码加密缓存
     */
    String LOGIN_TOKEN = "auth:oauth:loginToken:";
    /**
     * 用户登陆可以用资源缓存
     */
    String USER_LOGIN_RESOURCE = "auth:oauth:user:loginResource:";
    /**
     * 用户分配资源
     */
    String USER_RESOURCE = "auth:oauth:user:resource:";
}
