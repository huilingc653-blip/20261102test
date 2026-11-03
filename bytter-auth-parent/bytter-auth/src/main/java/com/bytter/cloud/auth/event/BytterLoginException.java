package com.bytter.cloud.auth.event;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
/**
 * 自定义登陆失败异常
 *
 * @author mfs 2022-08-10
 * @since 1.0.0
 */
public class BytterLoginException extends InvalidRequestException {
    public BytterLoginException() {
        super("invalid_request");
    }
    public BytterLoginException(String msg) {
        super(msg);
    }
}
