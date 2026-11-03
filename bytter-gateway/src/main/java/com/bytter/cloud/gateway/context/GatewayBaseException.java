package com.bytter.cloud.gateway.context;

public interface GatewayBaseException {
    /**
     * 返回code
     *
     * @return 代码
     */
    String getCode();

    /**
     * 返回msg
     *
     * @return 信息
     */
    String getMsg();
}
