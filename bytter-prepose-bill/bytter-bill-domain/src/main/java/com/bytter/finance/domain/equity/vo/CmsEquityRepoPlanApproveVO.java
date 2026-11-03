package com.bytter.finance.domain.equity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 股权融资回购计划-审批详情页面数据
 */
@Data
public class CmsEquityRepoPlanApproveVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 回购计划主表 */
    private CmsEquityRepoPlanVO repoPlanVO;
    /** 融资明细 */
    private CmsEquityInfoDetailVO infoDetail;
    /** 审批任务id */
    private String taskId;
}
