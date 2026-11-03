package com.bytter.cloud.gateway.context;


import lombok.Getter;

/**
 * 通用错误异常定义
 *
 * @author 刘卫锋 2022-03-01
 * @since 1.0.0
 */
@Getter
public enum GatewayCodeEnum implements GatewayBaseException {
    IP_CHECK_FAIL("4003", "非法ip访问，请联系管理员"),
    IP_FAIL("4004", "ip或域名填写错误"),
    TOKEN_EX_FAIL("4001", "token失效或者已过期"),
    TOKEN_CONFLICT("4005", "登录凭据不一致，请重新登录。"),
    COMMON_TIPS_SUCCESS("CM2000", "处理成功"),
    COMMON_AUTH_LIMIT("4003", "权限不足，您不具备当前操作权限！"),
    ARGUMENT_NOT_VALID("CM4000", "请求参数校验不通过"),
    UPLOAD_FILE_SIZE_LIMIT("CM4001", "上传文件大小超过限制"),
    COMMON_TIPS_FAIL("CM5000", "操作失败!"),
    GATEWAY_ERROR("CM5002", "网关异常"),
    GATEWAY_NOT_FOUND_SERVICE("CM5003", "服务未找到"),
    AUTH_NOT_FOUND_SERVICE("CM5003", "鉴权服务实例未找到"),
    SERVICE_UNAVAILABLE("CM5003", "服务未找到"),
    GATEWAY_CONNECT_TIME_OUT("CM5004", "网关超时"),
    COMMON_TIPS_UNKNOWN_EXCEPTION("CM5001", "系统开小差啦，我们已经记录了系统相关信息，请联系管理员！"),
    CAS_CHECK_EXCEPTION("CM5001", "cas认证失败！"),
	 XSS_SQL_CHECK_EXCEPTION("CM5010", "xss 或 sql注入非法请求");


    /**
     * 错误类型码
     */
    private final String code;

    /**
     * 错误类型描述信息
     */
    private final String msg;

    /**
     * 构造器
     *
     * @param code 错误代码
     * @param msg  错误信息
     */
    GatewayCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
