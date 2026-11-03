package com.bytter.core.common.queue;

import com.bytter.core.base.entity.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Data
public class CommonMqMsg<T> implements Serializable {

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 发送消息体 需要实现序列化接口
     */
    private T data;

    private String from;

    private String to;

    private Type type;

    /**
     * 消息发送状态 后续入库需要
     *
     */
    private Integer status;

    @AllArgsConstructor
    @Getter
    public enum Type{

        BIS_EXC("付款指令状态变更消息"),
        BIS_GATHER_EXC("收款状态变更消息"),
        BIS_FOREIGN_EXC("外汇状态变更消息"),
        BIS_FINANCE_EXC("理财指令表更")
        ;
        private final String desc;
    }

    /**
     * 通用构建消息方法
     * @param type 消息类型
     * @param data 消息数据
     * @return 消息体
     * @param <T>
     */
    public static <T> CommonMqMsg<T> buildMsg(Type type,T data) {
        CommonMqMsg<T> commonMqMsg = new CommonMqMsg<>();
        commonMqMsg.setMsgId(UUID.randomUUID().toString());
        commonMqMsg.setType(type);
        commonMqMsg.setData(data);
        return commonMqMsg;
    }

    public static <T> CommonMqMsg<T> buildMsg(T data) {
        CommonMqMsg<T> commonMqMsg = new CommonMqMsg<>();
        commonMqMsg.setMsgId(UUID.randomUUID().toString());
        commonMqMsg.setData(data);
        return commonMqMsg;
    }
}
