package com.bytter.biz.transfer.swap.enums;
/**
 * Created by zhouj on 2018/5/30.
 * 类型类型: payer付款；payee收款;accebill承兑汇票;
 */
public enum ErpInterConfigEnum {
    //付款
    PAYER("payer"),
    //收款
    PAYEE("payee"),
    //承兑汇票
    ACCEBILL("accebill");
    ErpInterConfigEnum(String code) {
        this.code = code;
    }
    private String code;
    public String getCode() {
        return code;
    }
}
