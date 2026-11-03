package com.bytter.core.common.exception;


/**
 * 自定义登陆失败异常
 *
 * @author mfs 2022-08-10
 * @since 1.0.0
 */
public class BytterLoginException extends RuntimeException {

    public BytterLoginException(String msg) {
        super(msg);
    }

}
