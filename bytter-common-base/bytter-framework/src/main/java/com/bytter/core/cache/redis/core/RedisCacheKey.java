package com.bytter.core.cache.redis.core;

import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RedisCacheKey {
    URL_PERMISSION_MAPPING_ALL("url:permission:mapping:all:%s", "系统里面URL与资源映射关系"),
    RESOURCES_PERMISSION_PREFIX("resources:permission:%s","用户资源缓存"),
    SM2_KEY("sm2:key:%s", "密码加密公私钥"),
    LOCK_SCREEN("lock:screen", "用户锁屏状态"),
    MUL_LANG_PRE("mul:lang:", "国际化数据"),
    EXCEL_CACHE_PRE("excel:import:cache:%s", "excel模版导入缓存"),
    IP_WHITE_SWITCH("ip:white:switch", "IP白名单开关"),
    IP_WHITE_ALL("ip:white:all", "IP白名单"),
    USER_SYS_SKIN("user:sys:skin", "用户浏览器皮肤"),
    SYS_CURRENCY("sys:currency", "币种缓存"),
    SYS_CORP("sys:corp", "单位缓存"),
    THIRD_LOGIN_TOKEN("third:login:token","第三方H5登陆token缓存"),
    THIRD_DSL_API_TOKEN("third:dsl:api:token","第三方客户（大参林）API接口token缓存"),
    WORKFLOW_CONSUMER_REPEAT("workflow:consumer:repeat","工作流消息防重"),
    ;

    private final String code;

    private final String desc;

    /**
     * 拼接对应服务名 方便管理 会默认加上服务名需要注意
     * @param redisCacheKey 建议冒号隔离 文件夹管理
     * @return 缓存key
     */
    public static String makeRedisKey(RedisCacheKey redisCacheKey,String ...suffix) {
        String appName = ApplicationContextUtil.getContext().getEnvironment()
                .getProperty("spring.application.name", "");
        if (suffix.length == 0) {
            return appName + CommonConstant.COLON_SPLIT + redisCacheKey.getCode();
        }
        return appName + CommonConstant.COLON_SPLIT + String.format(redisCacheKey.getCode(), suffix);
    }

    /**
     * 拼接对应服务名 方便管理 不加上服务名前缀
     * @param redisCacheKey 建议冒号隔离 文件夹管理
     * @return 缓存key
     */
    public static String makeRedisKey(String prefix,RedisCacheKey redisCacheKey,String ...suffix) {
        prefix = StringUtils.isBlank(prefix) ? "common" : prefix;
        if (suffix.length == 0) {
            return prefix + CommonConstant.COLON_SPLIT + redisCacheKey.getCode();
        }
        return prefix + CommonConstant.COLON_SPLIT + String.format(redisCacheKey.getCode(), suffix);
    }

}
