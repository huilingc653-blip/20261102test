package com.bytter.biz.erp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 银行账户交易明细vo
 *
 * @author fangcf
 * @date 2026-05-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErpBisAccDtlVo {
    /***
     * ID
     */
    private String serialId;
    /**
     * 本方账号
     */
    private String bankAcc;

    /**
     * 本方账户名
     */
    private String accName;

    /**
     * 本方账户开户行
     */
    private String bankName;

    /**
     * 交易金额
     */
    private BigDecimal amt;

    /**
     * 对方账号
     */
    private String oppAccNo;

    /**
     * 对方账户名
     */
    private String oppAccName;

    /**
     * 对方账户开户行
     */
    private String oppAccBank;

    /**
     * 交易时间
     */
    private String transTime;

    /**
     * 借贷标志
     */
    private String cdSign;

    /**
     * 银行流水号
     */
    private String bankSerialNo;

    /**
     * 用途
     */
    private String uses;

    /**
     * 摘要
     */
    private String remark;

    /**
     * 附言
     */
    private String postscript;
}
