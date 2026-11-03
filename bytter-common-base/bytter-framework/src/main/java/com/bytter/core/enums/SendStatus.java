package com.bytter.core.enums;

/**
 * @author luoyj
 * @date 2024/3/1.
 */
public enum SendStatus {

    N_SEND("未发送"),
    ACK("消息已成功发送到MQ服务"),
    NACK("消息未成功发送到MQ服务");

    private final String name;

    SendStatus(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
