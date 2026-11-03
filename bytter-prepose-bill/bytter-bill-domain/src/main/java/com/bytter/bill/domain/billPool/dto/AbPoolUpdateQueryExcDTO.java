package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新票据池查询指令状态参数
 */
@Data
public class AbPoolUpdateQueryExcDTO implements Serializable {
    private String busId;
    private String reCode;
    private String reMsg;
}
