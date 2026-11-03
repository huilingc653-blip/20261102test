package com.bytter.cloud.auth.constants;
/**
 * 用户常量类
 *
 * @author chenlw 2021-03-26
 * @since 1.0.0
 */
public interface UserConstant {
    /**
     * 是否有效用户：1可用 ；0不可用；
     */
    String STATUS_ENABLE = "1";
    /**
     * 是否有效用户：1可用 ；0不可用；
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
     * 是否是管理员：1是 ；0不是；
     */
    String IS_ADMIN_YES = "1";
    /**
     * 是否是管理员：1是 ；0不是；
     */
    String IS_ADMIN_NO = "0";
    /**
     * 用户账号是否过期：1未过期；0已过期；
     */
    String ACCOUNT_NON_EXPIRED_YES = "1";
    /**
     * 用户账号是否过期：1未过期；0已过期；
     */
    String ACCOUNT_NON_EXPIRED_NO = "0";
    /**
     * 用户密码是否过期：1未过期；0已过期；
     */
    String CREDENTIALS_NON_EXPIRED_YES = "1";
    /**
     * 用户密码是否过期，true为未过期   1未过期；0已过期；
     */
    String CREDENTIALS_NON_EXPIRED_NO = "0";
    /**
     * 用户账号是否被锁定：1未锁定；0已锁定；
     */
    String ACCOUNT_NON_LOCKED_YES = "1";
    /**
     * 用户账号是否被锁定：1未锁定；0已锁定；
     */
    String ACCOUNT_NON_LOCKED_NO = "0";
    /**
     * 默认证书
     */
    String CA_CERT_NUMBER = "null";
    /**
     * 用户登录方式：0用户密码方式
     */
    String LOGIN_TYPE_PASSWORD = "0";
    /**
     * 用户登录方式：1证书认证＋用户密码方式
     */
    String LOGIN_TYPE_PASSWORD_CERT = "1";
    /**
     * 用户登录方式：2证书认证方式
     */
    String LOGIN_TYPE_CERT = "2";
    /**
     * 用户登录方式：3手机登陆
     */
    String LOGIN_TYPE_MOBILE = "3";
}
