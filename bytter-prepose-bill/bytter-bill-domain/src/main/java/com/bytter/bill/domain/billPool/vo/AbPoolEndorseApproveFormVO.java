package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 背书申请 审批详情/手机审批页面数据(共用)
 */
@Data
public class AbPoolEndorseApproveFormVO implements Serializable {
    private AbPoolEndorseVO abPoolEndorseVO;
    private AbPoolEndorseVO billInfo;
    private Boolean isApprove;
    private String taskId;
}
