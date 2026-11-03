package com.bytter.core.base.entity;

import java.io.Serializable;

/**
 * 当前用户接口
 *
 * @author zhoul
 * @since 2018/1/3.
 */
public interface ICurrentUser extends Serializable {
    /**
     * 获取当前用户ID
     *
     * @return 用户ID
     */
    String getId();

    /**
     * 获取当前用户登录名
     *
     * @return 当前用户登录名
     */
    String getLoginName();

    /**
     * 获取当前用户名称
     *
     * @return 当前用户名称
     */
    String getUsername();

    /**
     * 获取当前用户单位ID
     *
     * @return 当前用户单位ID
     */
    String getCorpId();

    String getTenantId();

    /**
     * 是否管理员
     */
    default boolean isAdmin(){
        return true;
    };

}
