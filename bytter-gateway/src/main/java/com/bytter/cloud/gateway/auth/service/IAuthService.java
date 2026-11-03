package com.bytter.cloud.gateway.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * 授权服务接口
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
public interface IAuthService {

    /**
     * 判断url是否在忽略的范围内
     * 只要是配置中的开头，即返回true
     *
     * @param url 地址
     * @return true忽略
     */
    boolean ignoreAuthentication(String url);

    /**
     * 调用签权服务，判断用户是否有权限
     *
     * @param uid            用户ID
     * @param url            地址
     * @param method         方法
     * @return true/false
     */
    boolean hasPermission(String uid, String url, String method);
    boolean hasPermissionV1(String uid, String url, String method,String tenantId);

    /**
     * 校验token
     *
     * @param tokenValue token
     * @return true/false
     */
    boolean checkToken(String tokenValue);

    /**
     * 是否无效authentication
     *
     * @param authentication token信息
     * @return true有效，false无效
     */
    Jws<Claims> invalidJwtAccessToken(String authentication);

    /**
     * 从认证信息中提取jwt token 对象
     *
     * @param jwtToken toke信息 header.payload.signature
     * @return Jws对象
     */
    Jws<Claims> getJwt(String jwtToken);

    /**
     * 获取Ip白名单配置
     */
    String ipWhiteList();

    /**
     * 检测Ip是否合法
     */
    Boolean checkIp(String remoteIp);

    /**
     * 用户token过期通知
     * @param userId 用户ID
     */
    Boolean tokenExpiredNotify(String userId);

}
