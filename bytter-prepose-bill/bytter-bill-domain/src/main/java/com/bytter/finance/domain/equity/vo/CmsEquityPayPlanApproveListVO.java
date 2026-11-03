package com.bytter.finance.domain.equity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 缴付/过户计划-审批列表页面数据
 */
@Data
public class CmsEquityPayPlanApproveListVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 审批状态 */
    private String flowStatus;
    /** 周期时间 */
    private String cycleTime;
}
