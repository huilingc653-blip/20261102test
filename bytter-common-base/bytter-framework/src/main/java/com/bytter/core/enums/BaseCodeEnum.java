package com.bytter.core.enums;


import com.bytter.cloud.common.core.exception.ErrorType;

import lombok.Getter;

/**
 * 通用错误异常定义
 *
 * @author 刘卫锋 2022-03-01
 * @since 1.0.0
 */
@Getter
public enum BaseCodeEnum implements ErrorType {

    TOKEN_EX_FAIL("4001", "token失效或者已过期"),
    COMMON_TIPS_SUCCESS("CM2000", "处理成功"),
    LOGIN_FAILED("CM2001", "登录验证失败，请检查用户名和密码是否正确"),
    COMMON_AUTH_LIMIT("4003", "权限不足，您不具备当前操作权限！"),
    ARGUMENT_NOT_VALID("CM4000", "请求参数校验不通过"),
    UPLOAD_FILE_SIZE_LIMIT("CM4001", "上传文件大小超过限制"),
    INVALID_TOKEN("CM4002", "令牌已过期或无效"),
    COMMON_TIPS_FAIL("CM5000", "操作失败!"),
    GATEWAY_ERROR("CM5002", "网关异常"),
    GATEWAY_NOT_FOUND_SERVICE("CM5003", "服务未找到"),
    SERVICE_UNAVAILABLE("CM5003", "服务未找到"),
    GATEWAY_CONNECT_TIME_OUT("CM5004", "网关超时"),
    DUPLICATE_PRIMARY_KEY("CM5050", "唯一键冲突"),
    COMMON_TIPS_UNKNOWN_EXCEPTION("CM5001", "系统开小差啦，我们已经记录了系统相关信息，请联系管理员！");
	

    /**
     * 错误类型码
     */
    private String code;

    /**
     * 错误类型描述信息
     */
    private String msg;

    /**
     * 构造器
     *
     * @param code 错误代码
     * @param msg  错误信息
     */
    BaseCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
