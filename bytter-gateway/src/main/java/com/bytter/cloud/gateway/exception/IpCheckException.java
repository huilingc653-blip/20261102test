package com.bytter.cloud.gateway.exception;

import com.bytter.cloud.gateway.context.GatewayBaseException;

public class IpCheckException extends RuntimeException{

    private GatewayBaseException gatewayBaseException;

    public IpCheckException() {

    }

    public IpCheckException(GatewayBaseException gatewayBaseException) {
        this.gatewayBaseException = gatewayBaseException;
    }

    @Override
    public String getMessage() {
        return this.gatewayBaseException.getMsg();
    }
}
