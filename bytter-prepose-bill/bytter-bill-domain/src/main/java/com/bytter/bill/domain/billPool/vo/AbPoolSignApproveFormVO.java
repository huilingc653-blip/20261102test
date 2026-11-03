package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.vo.AbBillReceivebizVO;
import lombok.Data;

import java.io.Serializable;

/**
 * 电票签收 审批详情/手机审批页面数据(共用)
 */
@Data
public class AbPoolSignApproveFormVO implements Serializable {
    private AbPoolSignVO abPoolSignVO;
    private AbBillReceivebizVO info;
    private Boolean isApprove;
    private String taskId;
}
