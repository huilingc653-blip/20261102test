package com.bytter.biz.payment.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("对账提交请求体")
public class CorpCompareBillSubmitDTO {

    private String id;

    private String billId;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 账号
     */
    private String account;
    /**
     * 账号余额
     */
    private BigDecimal balance;
    /**
     * 企业账面余额
     */
    private BigDecimal erpBalance;
    /**
     * 核对结果: 1 相符、0 不相符
     */
    private String isCheck;
    /**
     * 中心借方未达帐
     */
    private String accountDebitMoney;
    /**
     * 中心贷方未达帐
     */
    private String accountCreditMoney;
    /**
     * 单位借方未达帐
     */
    private String erpDebitMoney;
    /**
     * 单位贷方未达帐
     */
    private String erpCreditMoney;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 操作人
     */
    private String opCode;
    /**
     * 操作日期
     */
    private Date opDate;
    /**
     * 审批日期
     */
    private Date checkDate;
    /**
     * 打印次数
     */
    private Integer prints;
    /**
     * 账单月份
     */
    private String billMonth;

    private String compareDate;
}
