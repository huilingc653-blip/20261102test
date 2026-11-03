package com.bytter.auth.api;

import java.util.List;
import java.util.Map;

/**
 * 鉴权服务类
 *
 */
public interface ApiIAuthenticationService {
    
    /**
     * 租户退出
     *
     * @param tenantId 租户ID
     * @param userName 用户账号
     * @return 返回是否退出成功
     */
    boolean tenantLogout(String tenantId, String userName);

    /**
     * 租户退出
     *
     * @param tenantIds 租户ID列表
     * @return 返回是否退出成功
     */
    boolean tenantLogouts(List<String> tenantIds);

    /**
     *
     * 租户退出
     *
     * @param tenantId
     * @param clientIds
     * @return
     */
    boolean tenantLogouts(String tenantId, List<String> clientIds);

    /**
     * 请求url与权限code映射关系
     * @return
     */
    Map<Object, Object> getUrlPermissionMappingV1(String tenantId);
    Map<Object, Object> getUrlPermissionMapping();
    /**
     * 判断用户是否有资源访问权限
     * @return
     */
    Boolean hasPermission(String uid, String url, String method);
    Boolean hasPermissionV1(String uid, String url, String method,String tenantId);


    /**
     * 获取系统IP白名单配置
     */
    String ipWhiteList();

    /**
     * 检测Ip是否合法
     * @return
     */
    Boolean checkIp(String remoteIp);

    /**
     * 用户token过期通知
     * @param userId 用户ID
     */
    Boolean tokenExpiredNotify(String userId);

    /**
     * 判断token是否失效。
     *
     * @param  tokenValue token值。
     * @return true: 失效，false: 未失效
     */
    boolean checkToken(String tokenValue);

}
