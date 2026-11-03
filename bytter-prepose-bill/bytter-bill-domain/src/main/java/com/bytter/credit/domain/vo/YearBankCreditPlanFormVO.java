package com.bytter.credit.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 银行授信年度计划 新增/编辑页面数据(共用)
 */
@Data
public class YearBankCreditPlanFormVO implements Serializable {
    private YearBankCreditPlanVO info;
    private Integer currentYear;
}
