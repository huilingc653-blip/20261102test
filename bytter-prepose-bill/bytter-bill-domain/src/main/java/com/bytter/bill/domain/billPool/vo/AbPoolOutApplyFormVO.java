package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import lombok.Data;

import java.io.Serializable;

/**
 * 出池申请 详情/审批详情/手机审批页面数据(共用)
 */
@Data
public class AbPoolOutApplyFormVO implements Serializable {
    private AbReceiveBillVO abReceiveBillPage;
    private Boolean isApprove;
    private AbPoolOutApplyVO poolDTO;
    private String taskId;
}
