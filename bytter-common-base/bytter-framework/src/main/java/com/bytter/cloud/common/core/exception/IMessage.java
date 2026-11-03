package com.bytter.cloud.common.core.exception;
/**
 * 抛出的异常，转换为国际化的Key值
 *
 * @author zhoul 2021-03-15
 * @since 1.0.0
 */
public interface IMessage {
    /**
     * 枚举名称，或枚举中定义的值
     *
     * @return 枚举名称
     */
    String name();
    /**
     * 国际化键，如果有则优先使用该值
     * @return 国际化键
     */
    default String getName() {
        return null;
    }
}
