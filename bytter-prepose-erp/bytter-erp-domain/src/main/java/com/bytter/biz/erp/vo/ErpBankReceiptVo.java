package com.bytter.biz.erp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErpBankReceiptVo implements java.io.Serializable {

    /**
     * 电子回单唯一键
     */
    private String serialId;

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
     * 文件类型: pdf,png,jpg键
     */
    private String fileType;
    private String filePath;

    /**
     * 收付标识 0收 1付
     */
    private String cdSign;

    /**
     * 记账日期
     */
    private String chargeDate;

    private byte[] ebillimg;

}
