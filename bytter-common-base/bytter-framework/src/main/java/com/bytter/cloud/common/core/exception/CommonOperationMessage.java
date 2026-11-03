package com.bytter.cloud.common.core.exception;
/**
 * 公共的操作提醒
 *
 * @author zhoul 2021-03-15
 * @since 1.0.0
 */
public enum CommonOperationMessage implements IMessage {
    /**
     * 操作成功
     */
    COMMON_TIPS_SUCCESS,
    /**
     * 操作失败
     */
    COMMON_TIPS_FAIL,
    /**
     * 系统开小差啦，我们已经记录了系统相关信息，请联系管理员！
     */
    COMMON_TIPS_UNKNOWN_EXCEPTION,
}
