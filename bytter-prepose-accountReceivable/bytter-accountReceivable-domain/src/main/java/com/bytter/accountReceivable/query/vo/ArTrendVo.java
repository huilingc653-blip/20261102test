package com.bytter.accountReceivable.query.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页-应收趋势分析
 */
@Data
public class ArTrendVo {

    /**
     * 币种，用于中间环节计算
     */
    private String curId;

    /**
     * 月份
     */
    private String month;

    /**
     * 应收余额
     */
    private BigDecimal writeOffBal;

    /**
     * 合同金额
     */
    private BigDecimal contractMoney;

    /**
     * 本期新增应收
     */
    private BigDecimal receivableMoney;

    /**
     * 本期核销金额
     */
    private BigDecimal writeOffAmount;
}
