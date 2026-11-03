package com.bytter.cloud.gateway.exception;

import com.bytter.cloud.gateway.context.GatewayBaseException;

public class XssAndSqlException extends RuntimeException{

    private GatewayBaseException gatewayBaseException;

    public XssAndSqlException(GatewayBaseException gatewayBaseException) {
        this.gatewayBaseException = gatewayBaseException;
    }

    @Override
    public String getMessage() {
        return this.gatewayBaseException.getMsg();
    }
}
