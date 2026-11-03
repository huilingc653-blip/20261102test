package com.bytter.finance.domain.provide.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2024
 *
 * @author ZRS
 * @date 2024/10/21
 */
@Data
public class MonthlyLoanVO implements Serializable {
    private String cellName;
    @BtCompositeSearch(fieldName = "corp.id")
    private String corpId;
    @BtCompositeSearch(fieldName = "corp.NAME")
    private String corpName;
    // 单位代码
    private String corpCode;
    // 借款类型
    @BtCompositeSearch(fieldName = "info.PL_CLASS")
    private String plClass;
    // 借款类型名称
    private String plClassName;
    private String bankName;
    @BtCompositeSearch(fieldName = "send.ONLINE_PAYMENT_ACC")
    private String sendAccNo;
    @BtCompositeSearch(fieldName = "send.MONEY")
    // 发放金额
    private BigDecimal sendMoney;
    // 已还款金额
    private BigDecimal sumRepayMoney;
    // 待还款金额
    private BigDecimal sumUnRepayMoney;
    private String plTerm;
    @BtCompositeSearch(fieldName = "send.SEND_DATE")
    private String sendDate;
    @BtCompositeSearch(fieldName = "send.REPAY_DATE")
    private Date repayDate;
    @BtCompositeSearch(fieldName = "send.REPAY_DATE")
    private String repayDateStr;
    private BigDecimal plRate;
    // 最新利率
    private BigDecimal latestRate;
    private String rmk;
    private String sendUser;
    private String ternUnit;

    // 到期日期开始时间
    private Date startRepayDate;

    // 到期日期结束时间
    private Date endRepayDate;

    // 默认到期日期开始时间
    private Date defaultStartRepayDate;

    // 默认到期日期结束时间
    private Date defaultEndRepayDate;

    // 点击查询按钮时查询标识
    private String queryFlag;

    public MonthlyLoanVO() {

    }

    public MonthlyLoanVO(String corpName, String plClassName, BigDecimal sendMoney, BigDecimal sumRepayMoney, BigDecimal sumUnRepayMoney) {
        this.corpName = corpName;
        this.plClassName = plClassName;
        this.sendMoney = sendMoney;
        this.sumRepayMoney = sumRepayMoney;
        this.sumUnRepayMoney = sumUnRepayMoney;
    }
}
