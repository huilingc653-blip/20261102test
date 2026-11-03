package com.bytter.cloud.common.core.exception;
import lombok.Getter;
/**
 * 系统消息
 *
 * @author chenlw 2021-04-15
 * @since 1.0.0
 */
@Getter
public enum SystemMessage implements IMessage {
    /**
     * 系统繁忙,请稍候再试
     */
    SYSTEM_BUSY,
    /**
     * 服务未找到
     */
    GATEWAY_NOT_FOUND_SERVICE,
    /**
     * 网关异常
     */
    GATEWAY_ERROR,
    /**
     * 网关超时
     */
    GATEWAY_CONNECT_TIME_OUT,
    /**
     * 请求参数校验不通过
     */
    ARGUMENT_NOT_VALID,
    /**
     * 无效token
     */
    INVALID_TOKEN,
    /**
     * 过期token
     */
    EXPIRED_TOKEN,
    /**
     * 上传文件大小超过限制
     */
    UPLOAD_FILE_SIZE_LIMIT,
    /**
     * 唯一键冲突
     */
    DUPLICATE_PRIMARY_KEY
}
