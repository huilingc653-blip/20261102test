package com.bytter.core.common.exception;

/**
 * 封装Excel异常父类，用于捕获Excel异常信息。
 *
 * @author zhaohc
 * @date 2024/1/11 16:06
 */
public abstract class ExcelException extends RuntimeException{
    public ExcelException() {
        super();
    }
    public ExcelException(String message) {
        super(message);
    }
    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }
}
