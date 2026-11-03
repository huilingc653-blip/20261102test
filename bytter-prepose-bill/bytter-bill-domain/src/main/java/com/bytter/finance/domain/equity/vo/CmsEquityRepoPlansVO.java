package com.bytter.finance.domain.equity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 股权融资回购-回购计划明细页面数据
 */
@Data
public class CmsEquityRepoPlansVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 回购计划明细列表 */
    private List<CmsEquityRepoPlanDetailVO> planDetailList;
    /** 选中的回购计划明细id */
    private String detailPlanID;
}
