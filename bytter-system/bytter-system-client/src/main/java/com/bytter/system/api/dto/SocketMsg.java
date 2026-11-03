package com.bytter.system.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * 简单消息通用发送实体
 */
@Data
@Builder
public class SocketMsg implements Serializable {

    /**
     * 消息内容
     */
    private String msg;

    private Type type;

    public static SocketMsg build(Type type) {
        return SocketMsg.builder().type(type).build();
    }

    public static SocketMsg build(Type type,String msg) {
        return SocketMsg.builder().type(type).msg(msg).build();
    }


    @AllArgsConstructor
    @Getter
    public enum Type{
        HOME_NOTICE("首页待办数据刷新"),
        UI_UPDATE("系统风格切换"),
        KICK_OUT("单点踢人"),
        TOKEN_EX("token失效"),
        ;
        private final String desc;

    }
}
