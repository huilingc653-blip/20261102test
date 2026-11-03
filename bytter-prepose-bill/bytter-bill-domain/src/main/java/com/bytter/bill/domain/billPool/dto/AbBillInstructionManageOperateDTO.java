package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 指令处理操作参数
 */
@Data
public class AbBillInstructionManageOperateDTO implements Serializable {
    private String id;
    private String type;
    private String status;
}
