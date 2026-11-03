package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 贴现申请 审批详情/手机审批页面数据(共用)
 */
@Data
public class AbPoolDiscountApproveFormVO implements Serializable {
    private AbPoolDiscountVO abPoolDiscountVO;
    private AbPoolDiscountVO billInfo;
    private Boolean isApprove;
    private String taskId;
}
