package com.bytter.accountReceivable.query.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页应收账龄分布图vo
 */
@Data
public class ArAgingDistVo {

    /**
     * 币别id，用于中间环节计算
     */
    private String curId;

    /**
     * 账龄天数类型
     */
    private String agingDay;

    /**
     * 应收余额
     */
    private BigDecimal writeOffBal;
}
