package com.bytter.biz.erp.entity;

import lombok.Data;

@Data
public class ErpBankReceiptView implements java.io.Serializable {
    /**
     * 主键
     */
    private String serialId;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 付款账号
     */
    private String payerAccNo;

    /**
     * 付款户名
     */
    private String payerAccName;

    /**
     * 付款行
     */
    private String payerBankName;

    /**
     * 收款方账号
     */
    private String payeeAccNo;

    /**
     * 收款方户名
     */
    private String payeeAccName;

    /**
     * 收款行
     */
    private String payeeBankName;

    /**
     * 金额
     */
    private String amt;

    /**
     * 英文币别编码
     */
    private String curCode;

    /**
     * 回单编号
     */
    private String receiptNo;

    /**
     * 交易流水号(可用于流水匹配)
     */
    private String transSerialNo;

    /**
     * 收付标识 0收 1付
     */
    private String cdSign;

    /**
     * 记账日期
     */
    private String chargeDate;
}
