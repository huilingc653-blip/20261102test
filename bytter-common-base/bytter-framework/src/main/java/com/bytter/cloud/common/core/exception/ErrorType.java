package com.bytter.cloud.common.core.exception;
/**
 * 错误代码接口
 *
 * @deprecated 使用IMessage做替换
 * @author zhoul 2021-01-14
 * @since 1.0.0
 */
@Deprecated
public interface ErrorType extends IMessage {
    /**
     * 返回code
     *
     * @return 代码
     */
    String getCode();
    /**
     * 返回msg
     *
     * @return 信息
     */
    String getMsg();
}
