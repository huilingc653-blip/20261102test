package com.bytter.accountReceivable.query.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页-top10应收款比例分析用vo
 */
@Data
public class ArRateVo {
    /**
     * 客商名称
     */
    private String externalName;
    /**
     * 客商等级
     */
    private String creditRating;
    /**
     * 应收余额
     */
    private BigDecimal writeOffBal;
    /**
     * 合同金额
     */
    private BigDecimal contractMoney;
    /**
     * 逾期金额
     */
    private BigDecimal overDueAmount;
    /**
     * 比例：应收余额/合同金额 或 逾期金额/合同金额
     */
    private String rate;
}
