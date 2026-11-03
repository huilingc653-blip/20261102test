package com.bytter.cloud.common.core.crypto.cert.exception;
/**
 * 无效的X500名称异常
 *
 * @author zhoul 2021-03-05
 * @since 1.0.0
 */
public class InvalidX500NameException extends Exception {
    private static final long serialVersionUID = 3192247087539921768L;
    public InvalidX500NameException() {
        super();
    }
    public InvalidX500NameException(String message) {
        super(message);
    }
    public InvalidX500NameException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidX500NameException(Throwable cause) {
        super(cause);
    }
}
