package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 租金计划登记新增页面数据
 */
@Data
public class LeaseCapitalPlanAddVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private CmsFinancingLeaseInfoVO leaseInfoVo;
    private LeaseCapitalPlanVO leaseCapitalPlanVo;
}
