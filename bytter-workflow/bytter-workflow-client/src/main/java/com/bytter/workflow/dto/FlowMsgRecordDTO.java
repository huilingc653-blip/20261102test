package com.bytter.workflow.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 流程消息记录表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FlowMsgRecordDTO implements Serializable {

    private String cateKey;

    private String corpId;

    private String businessKey;

    /**
     * 消息状态 见枚举
     */
    private Integer status;

    private String msg;

    /**
     * 错误信息
     */
    private String exMsg;

}
