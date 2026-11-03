package com.bytter.workflow.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bytter.core.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 流程消息记录表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("act_extend_msg_record")
public class FlowMsgRecord extends BaseEntity {

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
