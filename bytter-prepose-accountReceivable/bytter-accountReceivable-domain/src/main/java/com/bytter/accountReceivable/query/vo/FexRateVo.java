package com.bytter.accountReceivable.query.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 首页应收款分析
 * 外币折合人民币用中间价
 */
@Data
public class FexRateVo {

    /**
     * 币种id
     */
    private String curId;

    /**
     * 中间价
     */
    private BigDecimal middleRate;
}
