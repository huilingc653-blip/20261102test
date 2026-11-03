package com.bytter.finance.domain.equity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 股权融资回购计划-审批列表页面数据
 */
@Data
public class CmsEquityRepoPlanApproveListVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 审批状态 */
    private String flowStatus;
    /** 周期时间 */
    private String cycleTime;
}
