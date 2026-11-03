package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.bill.domain.bill.vo.AbEmitBillVo;
import lombok.Data;

import java.io.Serializable;

/**
 * 跳转至手机审核详情界面
 */
@Data
public class MobileApproveVO implements Serializable {

    private AbEmitBillVo abEmitBillVo;
    private Boolean isApprove;
    private String payItem;
    private String businessName;
    private String processSysname;
    private String budgetDisplay;
    private String taskId;

}
