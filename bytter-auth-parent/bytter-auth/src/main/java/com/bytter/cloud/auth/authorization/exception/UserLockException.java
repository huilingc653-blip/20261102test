package com.bytter.cloud.auth.authorization.exception;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
/**
 * 用户被锁定抛出异常
 *
 * @author fei
 * @since 1.0.0
 */
public class UserLockException extends ClientAuthenticationException {
    public UserLockException(String msg) {
        super(msg);
    }
    @Override
    public String getOAuth2ErrorCode() {
        return "auth.user.locked";
    }
}
