package com.bytter.accountReceivable.query.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页-应收款周转分析
 */
@Data
public class ArTurnoverVo {

    /**
     * 应收账款周转率
     */
    private String arTurnoverRatio;

    /**
     * 平均应收账款余额
     */
    private BigDecimal averageArBalance;

    /**
     * 币种id，用于中间环节计算
     */
    private String curId;

    /**
     * 应收余额，用于中间环节计算
     */
    private BigDecimal writeOffBal;

    /**
     * 应收金额，用于中间环节计算
     */
    private BigDecimal receivableMoney;
}
