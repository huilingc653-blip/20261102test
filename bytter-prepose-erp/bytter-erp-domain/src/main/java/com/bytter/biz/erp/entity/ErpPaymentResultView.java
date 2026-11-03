package com.bytter.biz.erp.entity;

import lombok.Data;

import java.io.Serializable;


/**
 * Title: StandardPaymentResult
 * Description:  付款结果查询实体
 *
 * @author wangh
 * @date 2020-4-8
 */
@Data
public class ErpPaymentResultView implements Serializable {
    private String serialNoErp;// 流水号
    private String voucherNoErp;// 凭证号

    private String payerAccNo;// 付款方帐号
    private String payerBankName;// 付款方开户行
    private String payerAccName;// 付款方户名
    private String amt;// 交易金额

    private String payeeAccNo;// 收款方账号
    private String payeeAccName;// 收款方户名
    private String payeeBank;// 收款方开户行

    private String voucherStat;// 付款状态
    private String transTime;// 交易时间
    private String returnMsg;// 付款结果信息

    private String tenantId;
}
