package com.bytter.cloud.gateway.exception;

import com.bytter.cloud.gateway.context.GatewayBaseException;

public class AuthenticationException extends RuntimeException{

    private GatewayBaseException gatewayBaseException;

    public AuthenticationException(GatewayBaseException gatewayBaseException) {
        this.gatewayBaseException = gatewayBaseException;
    }

    @Override
    public String getMessage() {
        return this.gatewayBaseException.getMsg();
    }
}
