package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 租金计划详情页面数据
 */
@Data
public class LeaseCapitalPlanViewVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private CmsFinancingLeaseInfoVO leaseInfoVo;
    private List<LeaseCapitalPlanDelVO> leaseCapitalPlanDelVoList;
    private String leaseCapitalPlanDelId;
    /** 审批详情使用 */
    private String taskId;
}
