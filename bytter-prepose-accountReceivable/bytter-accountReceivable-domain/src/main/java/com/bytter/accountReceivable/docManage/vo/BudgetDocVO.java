package com.bytter.accountReceivable.docManage.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Title:
 * @Description:
 * @author yangxd
 * @date 2025-03-26
 * @version 1.0
 *
 */
@Data
public class BudgetDocVO implements Serializable {
    /**计划科目*/
    private String itemCode;
    /**计划科目名称*/
    private String itemName;
    /**执行金额*/
    private BigDecimal processMoney;
    /**汇率*/
    private BigDecimal exchangeRate;
    /**执行金额（折合本身币别）*/
    private BigDecimal processCurMoney;
}
