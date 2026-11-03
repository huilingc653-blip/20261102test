package com.bytter.report.domain.netbank.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 首页账户余额计算结果VO
 *
 * @author fangcf
 * @date 2025-07-31
 */
@Data
public class HomeBalCalculateResultVO implements Serializable {

    /**
     * 余额
     */
    private BigDecimal bal;

    /**
     * 币别
     */
    private String curId;

    /**
     * 收入金额
     */
    private BigDecimal inBal;

    /**
     * 支出金额
     */
    private BigDecimal outBal;
}
