package com.bytter.finance.domain.equity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 股权融资缴付到账-缴付计划明细页面数据
 */
@Data
public class CmsEquityPayThePlansVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 计划明细列表 */
    private List<CmsEquityPayPlanDetailVO> planDetailList;
    /** 选中的计划明细id */
    private String detailPlanID;
}
