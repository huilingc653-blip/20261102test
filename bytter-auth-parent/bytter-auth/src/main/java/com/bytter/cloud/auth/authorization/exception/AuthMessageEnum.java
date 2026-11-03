package com.bytter.cloud.auth.authorization.exception;
import com.bytter.cloud.common.core.exception.ErrorType;
import lombok.Getter;
/**
 * 授权信息错误代码
 *
 * @author zhoul 2021-1-19
 * @since 1.0.0
 */
@Getter
public enum AuthMessageEnum implements ErrorType {
    /**
     * 无效请求
     */
    AUTH_INVALID_REQUEST("AU5100", "auth.invalid.request"),
    /**
     * 无效client_id
     */
    AUTH_INVALID_CLIENT("AU5101", "auth.invalid.client"),
    /**
     * 无效授权
     */
    AUTH_INVALID_GRANT("AU5102", "auth.invalid.grant"),
    /**
     * 无效scope
     */
    AUTH_INVALID_SCOPE("AU5103", "auth.invalid.scope"),
    /**
     * 无效token
     */
    AUTH_INVALID_TOKEN("AU5104", "auth.invalid.token"),
    /**
     * 授权不足
     */
    AUTH_INSUFFICIENT_SCOPE("AU5105", "auth.insufficient.scope"),
    /**
     * redirect url不匹配
     */
    AUTH_REDIRECT_URI_MISMATCH("AU5106", "auth.redirect.uri_mismatch"),
    /**
     * 拒绝访问
     */
    AUTH_ACCESS_DENIED("AU5107", "auth.access.denied"),
    /**
     * 不支持该方法
     */
    AUTH_METHOD_NOT_ALLOWED("AU5108", "auth.access.denied"),
    /**
     * 权限服务错误
     */
    AUTH_SERVER_ERROR("AU5109", "auth.server.error"),
    /**
     * 未授权客户端
     */
    AUTH_UNAUTHORIZED_CLIENT("AU5110", "auth.unauthorized.client"),
    /**
     * 未授权
     */
    AUTH_UNAUTHORIZED("AU5111", "auth.unauthorized"),
    /**
     * 不支持的响应类型
     */
    AUTH_UNSUPPORTED_RESPONSE_TYPE("AU5112", "auth.unsupported.response.type"),
    /**
     * 不支持的授权类型
     */
    AUTH_UNSUPPORTED_GRANT_TYPE("AU5113", "auth.unsupported.grant.type"),
    /**
     * 租户不存在
     */
    AUTH_TENANT_DOES_NOT_EXIST("AU5114", "auth.tenant.does.not.exist"),
    /**
     * 租户已被禁用
     */
    AUTH_TENANT_DISABLED("AU5115", "auth.tenant.disabled"),
    /**
     * 租户无效
     */
    AUTH_TENANT_INVALID("AU5116", "auth.tenant.invalid"),
    /**
     * 租户已到期
     */
    AUTH_TENANT_EXPIRED("AU5117", "auth.tenant.expired"),
    /**
     * 用户无效
     */
    AUTH_USER_DISABLED("AU5118", "auth.user.disabled"),
    /**
     * 用户或密码错误
     */
    AUTH_BAD_CREDENTIAL("AU5119", "auth.bad.credential"),
    /**
     * 用户已被锁定
     */
    AUTH_USER_LOCKED("AU5120", "auth.user.locked"),
    /**
     * 证书无效
     */
    AUTH_ERROR_CERT_INVALID("AU5121", "auth.cert.invalid"),
    /**
     * 用户证书错误
     */
    AUTH_ERROR_CA_CERT_NUMBER("AU5122", "auth.ca.cert.number.is.error"),
    /**
     * 用户未绑定证书
     */
    AUTH_USER_NOT_BINDING_CERT("AU5123", "auth.user.not.binding.cert"),
    /**
     * 您输入的集团号不存在
     */
    AUTH_TENANTCODE_NOTEXIST("AU5124", "auth.tenantCode.notExist"),
    /**
     * 登陆失败，请您选择正确的登陆方式
     */
    AUTH_USER_LOGIN_TYPE_WRONG("AU5125", "auth.user.tips.loginTypeWrong"),
    /**
     * 登陆失败，请您选择正确的登陆方式
     */
    AUTH_USER_LOGIN_TYPE_PASSOWRD("AU5126", "auth.user.tips.loginTypePassword"),
    /**
     * 登陆失败，请您选择正确的登陆方式
     */
    AUTH_USER_LOGIN_TYPE_PASSOWRD_CERT("AU5127", "auth.user.tips.loginTypePasswordCert"),
    /**
     * 用户证书未生效
     */
    AUTH_ERROR_CA_CERT_BEGINDATE_BEFORE("AU5128", "auth.user.tips.certBeginDateBefore"),
    /**
     * 用户证书已过期
     */
    AUTH_ERROR_CA_CERT_ENDDATE_AFTER("AU5129", "auth.user.tips.certEndDateAfter"),
    /**
     * 用户证书日期错误
     */
    AUTH_ERROR_CA_CERT_DATE_ERROR("AU5130", "auth.user.tips.certDateError"),
    /**
     * 手机号码未绑定,不能登陆
     */
    AUTH_ERROR_PHONE_UNBIND("AU5131", "手机号码未绑定,不能登陆");
    /**
     * 错误码
     */
    private String code;
    private String msg;
    /**
     * 国际化
     */
    private String name;
    AuthMessageEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    static AuthMessageEnum getAuthErrorType(String oauth2ErrorCode) {
        AuthMessageEnum[] authErrorTypes = AuthMessageEnum.values();
        for (AuthMessageEnum authErrorType : authErrorTypes) {
            if (authErrorType.name.contains(oauth2ErrorCode)) {
                return authErrorType;
            }
        }
        return AuthMessageEnum.AUTH_INVALID_GRANT;
    }
}
