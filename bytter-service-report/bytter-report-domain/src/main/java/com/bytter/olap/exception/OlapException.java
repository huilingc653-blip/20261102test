package com.bytter.olap.exception;

/**
 * 自定义报表的异常类
 * @history
 *  1. 2010-01-09  chenlw  修改类名为OlapException
 */
public class OlapException extends RuntimeException {

    public OlapException(String message) {
        super(message);
    }

    public OlapException(String message, Throwable cause) {
        super(message, cause);
    }
}
