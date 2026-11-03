package com.bytter.system.api.constant;
/**
 * 用户常量类
 *
 * @author zuoy 2021-07-05
 * @since 1.0.0
 */
public interface TenantConstant {
    /**
     * 是否有效：1可用 ；0不可用；
     */
    String STATUS_ENABLE = "1";
    /**
     * 是否有效：1可用 ；0不可用；
     */
    String STATUS_DISABLE = "0";
    /**
     * 是否已删除：1已删除，0未删除
     */
    String DELETED_YES = "1";
    /**
     * 是否已删除：1已删除，0未删除
     */
    String DELETED_NOT = "0";
    /**
     * 是否配置踢出：1踢出，0不踢出
     */
    String KICK_OUT = "1";



    /**
     * 未签约
     */
    Integer TENANT_STATUS_UN_SIGNED = 1;
    /**
     * 已签约
     */
    Integer TENANT_STATUS_SIGNED = 2;
    /**
     * 禁用
     */
    Integer TENANT_STATUS_DISABLED = 3;
    /**
     * 已解约
     */
    Integer TENANT_STATUS_TERMINATED = 4;
}
