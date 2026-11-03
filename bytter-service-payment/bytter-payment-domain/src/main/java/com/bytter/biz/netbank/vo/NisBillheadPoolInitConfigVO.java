package com.bytter.biz.netbank.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class NisBillheadPoolInitConfigVO implements Serializable {
    private String budgetSel;
    private String payItem;
    private String accountOver;
    private String bankaccOver;
    private String showIsPayed;
    private String isOpenLimit;
    private String balControlType;
    // busType==0 时有值
    private String externalCorp;
    private String allowDifferentBillMoney;
    // busType==1(费用报销) 时有值
    private String expFbsin;
}
