package com.bytter.system.api.constant;
/**
 * 异常错误常量类
 *
 * @author zuoy 2021-07-05
 * @since 1.0.0
 */
public interface ErrorConstant {
    /**
     * 集团号不存在
     */
    String AUTH_TENANT_CODE_NOT_EXIST_MSG = "TenantCode does not exist";
    /**
     * 租户不存在
     */
    String AUTH_TENANT_NOT_EXIST_MSG = "Tenant does not exist";
    /**
     * 租户已被禁用
     */
    String AUTH_TENANT_DISABLED_MSG = "Tenant is disabled";
    
    /**
     * 租户无效
     */
    String AUTH_TENANT_INVALID_MSG = "Tenant is invalid";
    /**
     * 租户已到期
     */
    String AUTH_TENANT_EXPIRED_MSG = "Tenant expired";
    /**
     * 账号或密码错误
     */
    String AUTH_BAD_CREDENTIALS_MSG = "Bad credentials";
    /**
     * 用户已被禁用
     */
    String AUTH_ERROR_USER_IS_DISABLED_MSG = "User is disabled";
    /**
     * 用户未绑定证书
     */
    String AUTH_ERROR_USER_NOT_BINDING_CERT = "User not Binding Cert";
    /**
     * 证书无效
     */
    String AUTH_ERROR_CERT_INVALID = "Cert Invalid";
    /**
     * 用户证书错误
     */
    String AUTH_ERROR_CA_CERT_NUMBER = "CaCertNumber is error";
    /**
     * 用户已被锁定
     */
    String AUTH_ERROR_USER_LOCKED = "User has been locked";
    /**
     * 错误的登录类型方式
     */
    String AUTH_ERROR_LOG_TYPE = "LogType is wrong";
    /**
     * 错误的登录类型方式
     */
    String AUTH_ERROR_LOG_TYPE_PASSOWRD = "LogType is password";
    /**
     * 错误的登录类型方式
     */
    String AUTH_ERROR_LOG_TYPE_PASSOWRD_CERT = "LogType is passwordAndCert";
    /**
     * 无效的token
     */
    String AUTH_ERROR_TOKEN_INVALID = "invalid_token";
    /**
     * 用户证书未生效
     */
    String AUTH_ERROR_CA_CERT_BEGINDATE_BEFORE = "Cert beginDate before";
    /**
     * 用户证书已过期
     */
    String AUTH_ERROR_CA_CERT_ENDDATE_AFTER = "Cert endDate after";
    /**
     * 用户证书日期错误
     */
    String AUTH_ERROR_CA_CERT_DATE_ERROR = "Cert date error";
    /**
     * 未绑定手机号码
     */
    String AUTH_TENANT_PHONE_NOF_FOUND = "手机号码未绑定,不能登陆";
    /**
     * 无效的密码
     */
    String AUTH_ERROR_PWD_INVALID = "无效的密码";
    /**
     * manager服务异常
     */
    String MANAGER_SERVICE_ERR = "平台管理服务维护中，请稍后重试";
    /**
     * system服务异常
     */
    String SYSTEM_SERVICE_ERR = "系统管理服务维护中,请稍后重试";
    /**
     * 用户已被锁定
     */
    String AUTH_ERROR_USER_LOCKED_FMT = "用户已经被锁定，%s分钟后解锁";
}
