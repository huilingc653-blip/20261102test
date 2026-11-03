package com.bytter.core.common.exception;
/**
 * H5配置异常
 *
 * @author chenlw 2021-06-12
 * @since 1.0.0
 */
public class H5ConfigException extends Exception {
    public H5ConfigException(String message) {
        super(message);
    }
    public H5ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
    public H5ConfigException(Throwable cause) {
        super(cause);
    }
    public H5ConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
