package com.bytter.biz.erp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银行账户余额vo
 *
 * @author fangcf
 * @date 2026-05-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErpBisAccBalVo implements Serializable {
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
