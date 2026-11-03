package com.bytter.finance.domain.equity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 缴付/过户计划-审批详情页面数据
 */
@Data
public class CmsEquityPayPlanApproveVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 计划主表 */
    private CmsEquityPayPlanVO payPlanVO;
    /** 融资明细 */
    private CmsEquityInfoDetailVO infoDetail;
    /** 审批任务id */
    private String taskId;
}
