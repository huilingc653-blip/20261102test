package com.bytter.biz.erp.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银行账号余额视图
 */
@Data
public class ErpBisAccBalView implements Serializable {
    /**
     * 主键
     */
    private String serialId;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 余额日期
     */
    private String balDate;

    /**
     * 余额
     */
    private BigDecimal bal;

    /**
     * 可用余额
     */
    private BigDecimal availBal;

    /**
     * 冻结金额
     */
    private BigDecimal freezeBal;

    private String corpCode;
    private String corpName;

    private String curCode;
    private String curName;


}
