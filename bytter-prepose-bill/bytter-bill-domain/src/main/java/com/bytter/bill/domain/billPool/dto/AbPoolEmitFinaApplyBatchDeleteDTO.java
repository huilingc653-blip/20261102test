package com.bytter.bill.domain.billPool.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 融资申请批量删除参数
 */
@Data
public class AbPoolEmitFinaApplyBatchDeleteDTO implements Serializable {
    private String ids;
    private String reason;
}
