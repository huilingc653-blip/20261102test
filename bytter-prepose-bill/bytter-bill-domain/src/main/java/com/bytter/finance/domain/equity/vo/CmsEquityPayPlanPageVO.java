package com.bytter.finance.domain.equity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 缴付/过户计划-登记/查看页面数据
 */
@Data
public class CmsEquityPayPlanPageVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 融资明细 */
    private CmsEquityInfoDetailVO infoDetail;
    /** 计划主表 */
    private CmsEquityPayPlanVO payPlanVO;
    /** 是否审批查看 */
    private Boolean isApprove;
}
