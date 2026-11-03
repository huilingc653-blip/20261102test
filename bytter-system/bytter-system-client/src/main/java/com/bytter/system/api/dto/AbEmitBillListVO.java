package com.bytter.system.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbEmitBillListVO {
    private String appNo;
    private String rmk;
    private String batchNo;
    private String isBatchBill;
    private String payItem;
    private Boolean isRelateRat;
    private String curId;
    private Integer externalCorpNum;
    private Boolean isEmitMake;
    private Boolean isBail;
    private String bailBankControlType;
    /**
     * 票据系统参数配置：isOpenRat是否启用授信敞口(前置：挂接时候可操作)
     */
    private Boolean mouthRat;
    private Integer isGuarantee;
    private String isHangGuarantee;
    private ConfigDTO maxBillMoney;
    private Boolean isOpenCpRat;
    private String externalCorp;
    private String corpId;
    private BigDecimal payMoney;
    private String payeeacc;
    private String payeeAccName;
    private String payeebankno;
    private String payeeBankName;
    private String blRcvpymtpsUnnIdNo;
    private String isPayFlg;
    private String payeracc;
    private String payer;
    private String payerbankno;
    private String payerBankName;
    private String circulationMode;
    private String   bifCode;
    private String voucherId;
    /**
     * 支付结算参数配置：allowDifferentBillMoney-允许付款金额与票据金额不一致
     */
    private String allowDifferentBillMoney;
    private String applyCorpId;
    private Integer billSectionThreeButton;
    private Integer billSectionSixButton;
}
