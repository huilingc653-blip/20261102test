package com.bytter.accountReceivable.query.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页-应收账款分析实体
 */
@Data
public class ArAnalysisVo {

    /**
     * 币种id，用于中间环节计算，不需要传给前端
     */
    private String curId;

    /**
     * 应收余额
     */
    private BigDecimal writeOffBal;

    /**
     * 逾期金额
     */
    private BigDecimal overdueAmount;

    /**
     * 合同金额
     */
    private BigDecimal contractMoney;

}
