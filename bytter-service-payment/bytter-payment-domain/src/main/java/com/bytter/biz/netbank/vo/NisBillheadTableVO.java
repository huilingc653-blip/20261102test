package com.bytter.biz.netbank.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NisBillheadTableVO {
    private String acceBillCorpId;
    private String acceBillMoney;
    private String accountOver;
    private String bankaccOver;
    private String editPayerAcc;
    private String billFlag;
    private String billType;
    private String status;
    private String billFrom;
    private String curId;
    private String isForindividual;
    private String isPayed;
    private String payerAcc;
    private String minWishPayday;
    private String minPayMoney;
    private String corpId;
    private String shareBusTypeId;
    private String voucherTypeId;
    private String isOpenLimit;
    private String maxWishPayday;
    private String maxPayMoney;
    private String billNo;
    private String bursarBillNo;
    private String opCode;
    private String payeeAcc;
    private String payeeAccName;
    private String purpose;
    private String splitBillNo;
    private String opType;
    private String showIsPayed;
}
