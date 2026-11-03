package com.bytter.cloud.gateway.exception;
import com.bytter.cloud.gateway.context.GatewayBaseException;

public class TokenException extends RuntimeException{

    private GatewayBaseException gatewayBaseException;

    public TokenException(GatewayBaseException gatewayBaseException) {
        this.gatewayBaseException = gatewayBaseException;
    }

    @Override
    public String getMessage() {
        return this.gatewayBaseException.getMsg();
    }

    /**
     * 获取登录凭据错误类型，供统一异常处理区分过期与凭据冲突。
     *
     * @return 创建异常时指定的网关错误类型，不包含 token 内容
     */
    public GatewayBaseException getGatewayBaseException() {
        return gatewayBaseException;
    }

}
