package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 额度分配核准审核页面数据
 */
@Data
public class AbPoolLimitAllotCheckAuditVO implements Serializable {
    private String ids;
    private String applyMoney;
    private String num;
}
