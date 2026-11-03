package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 入池申请额度核准页面数据
 */
@Data
public class AbPoolLimitApplyCheckAuditVO implements Serializable {
    private String ids;
    private Integer abCount;
    private BigDecimal abBillMoney;
}
