package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 质押托管转换 审批详情/手机审批页面数据(共用)
 */
@Data
public class AbPoolPdTstApproveFormVO implements Serializable {
    private Object abReceiveBillPage;
    private AbPoolIntoApplyVO poolDTO;
    private Boolean isApprove;
    private String taskId;
    private List<AbPoolPdTstVO> tstList;
}
