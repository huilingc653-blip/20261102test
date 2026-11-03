package com.bytter.cloud.auth.event;
import org.springframework.context.ApplicationEvent;
/**
 * 自定义登陆失败事件
 *
 * @author mfs 2022-08-10
 * @since 1.0.0
 */
public class BytterLoginFailureEvent extends ApplicationEvent {
    private final BytterLoginException exception;
    public BytterLoginFailureEvent(BytterLoginException source) {
        super(source);
        this.exception = source;
    }
    public BytterLoginException getException() {
        return this.exception;
    }
}
