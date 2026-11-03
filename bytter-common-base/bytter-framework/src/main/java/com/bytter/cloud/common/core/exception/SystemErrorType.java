package com.bytter.cloud.common.core.exception;
import lombok.Getter;
/**
 * 系统错误枚举
 *
 * @author zhoul 2021-01-14
 * @since 1.0.0
 */
@Getter
public enum SystemErrorType implements ErrorType {
    // CPD-Ignore-On
    SERVER_ERROR("00000000", "处理失败"),
    SERVER_UNKNOWN_EXCEPTION("00000002", "系统开小差啦，我们已经记录了系统相关信息，请联系管理员！"),
    SERVER_SUCCESS("00000001", "处理成功"),
    SYSTEM_BUSY("00000010", "系统繁忙,请稍候再试"),
    GATEWAY_NOT_FOUND_SERVICE("00010404", "服务未找到"),
    GATEWAY_ERROR("00010500", "网关异常"),
    GATEWAY_CONNECT_TIME_OUT("00010002", "网关超时"),
    ARGUMENT_NOT_VALID("00020000", "请求参数校验不通过"),
    INVALID_TOKEN("00020001", "无效token"),
    UPLOAD_FILE_SIZE_LIMIT("00020010", "上传文件大小超过限制"),
    DUPLICATE_PRIMARY_KEY("00030000", "唯一键冲突");
    // CPD-Ignore-Off;
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
    SystemErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
