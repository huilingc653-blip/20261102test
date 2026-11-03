package com.bytter.finance.domain.equity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 股权融资回购计划-登记/查看页面数据
 */
@Data
public class CmsEquityRepoPlanPageVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 融资明细 */
    private CmsEquityInfoDetailVO infoDetail;
    /** 回购计划主表 */
    private CmsEquityRepoPlanVO repoPlanVO;
    /** 是否审批查看 */
    private Boolean isApprove;
}
