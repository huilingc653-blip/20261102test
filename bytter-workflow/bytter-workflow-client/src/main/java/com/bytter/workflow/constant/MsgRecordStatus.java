package com.bytter.workflow.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MsgRecordStatus {

    UN_SEND(0, "未找到路由消息配置"),
    SEND(1,"发送完成"),
    ACK(2,"消费完成"),
    UCK(3, "消费失败"),
    AUTO(4, "自动审批"),
    ;
    private final Integer code;
    private final String desc;
}
